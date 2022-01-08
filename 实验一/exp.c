#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <sys/wait.h>

#define ERROR_SYSCALL -1;

int drop_priv_temp(uid_t new_uid)
{
    if (setresuid(-1, new_uid, geteuid()) < 0)
        return ERROR_SYSCALL;
    if (geteuid() != new_uid)
        return ERROR_SYSCALL;
    return 0;
}

int drop_priv_perm(uid_t new_uid)
{
    uid_t ruid, euid, suid;
    if (setresuid(new_uid, new_uid, new_uid) < 0)
        return ERROR_SYSCALL;
    if (getresuid(&ruid, &euid, &suid) < 0)
        return ERROR_SYSCALL;
    if (ruid != new_uid || euid != new_uid || suid != new_uid)
        return ERROR_SYSCALL;
    return 0;
}

int restore_priv()
{
    int ruid, euid, suid;
    if (getresuid(&ruid, &euid, &suid) < 0)
        return ERROR_SYSCALL;
    if (setresuid(-1, suid, -1) < 0)
        return ERROR_SYSCALL;
    if (geteuid() != suid)
        return ERROR_SYSCALL;
    return 0;
}

int main(int argc, char *argv[])
{
    // 获得实际用户ID，有效用户ID，保存的用户ID
    uid_t ruid, euid, suid;
    getresuid(&ruid, &euid, &suid);
    printf("开始状态uid : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
    if (euid == 0)
    {
        // 使用root权限进入函数，临时性放弃root权限
        drop_priv_temp(1000);
        getresuid(&ruid, &euid, &suid);
        printf("临时性放弃root权限 : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
    }
    // 1. 调用bind绑定80端口需要设置 setuid 位, 否则会失败
    printf("1.设置setuid位场景\n");
    int socket_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (socket_fd < 0)
    {
        printf("socket error \n");
    }
    struct sockaddr_in server_sockaddr;
    memset(&server_sockaddr, 0, sizeof(server_sockaddr));
    server_sockaddr.sin_family = AF_INET;
    server_sockaddr.sin_port = htons(80);
    server_sockaddr.sin_addr.s_addr = inet_addr("127.0.0.1");
    //尝试绑定
    int ret = bind(socket_fd, (struct sockaddr *)&server_sockaddr, sizeof(server_sockaddr));
    if (ret < 0)
    {
        printf("bind error \n");
        if (suid == 0)
        {
            // 恢复root权限
            restore_priv();
            getresuid(&ruid, &euid, &suid);
            printf("恢复root权限 : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
            // 再次绑定
            ret = bind(socket_fd, (struct sockaddr *)&server_sockaddr, sizeof(server_sockaddr));
            if (ret < 0)
            {
                printf("bind error \n");
            }
            else
            {
                printf("bind success \n");
            }
        }
    }
    else
    {
        printf("bind success \n");
    }
    getresuid(&ruid, &euid, &suid);
    printf("调用bind函数后的 uid : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);

    // 2. 用户fork进程后，父进程和子进程中euid、ruid、suid的差别
    printf("2. 用户fork进程后，父进程和子进程中euid、ruid、suid的差别\n");
    if (fork() == 0)
    {
        getresuid(&ruid, &euid, &suid);
        printf("子进程 uid : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
        return 0;
    }
    else
    {
        getresuid(&ruid, &euid, &suid);
        printf("父进程 uid : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
    }
    wait(NULL);

    // 3. 利用 execl 执行 setuid 程序后，euid、ruid、suid是否有变化
    printf("3. 调用execl执行setuid函数前后，euid、ruid、suid是否有变化\n");
    if (fork() == 0)
    {
        getresuid(&ruid, &euid, &suid);
        printf("函数外调用execl执行setuid前 : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
        execl("setuid", "setuid", "1000", "0", "0", (char *)0);
        return 0;
    }
    wait(NULL);

    // 4. 两种放弃 root 权限的方式
    // 临时性放弃root权限
    printf("4. 两种放弃 root 权限的方式\n");
    drop_priv_temp(1000);
    getresuid(&ruid, &euid, &suid);
    printf("临时性放弃root权限 : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
    // 恢复root权限
    restore_priv();
    getresuid(&ruid, &euid, &suid);
    printf("恢复root权限 : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
    // 永久性放弃root权限
    drop_priv_perm(1000);
    getresuid(&ruid, &euid, &suid);
    printf("永久性放弃root权限 : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);

    // 5. 比较有环境变量和无环境变量的函数使用的差异。
    // 有环境变量的函数使用
    char *envp[] = {"USER=unknown", "PATH=/tmp", NULL};
    if (fork() == 0)
    {
        printf("5.1 有环境变量的函数使用\n");
        execle("exec_test", "exec_test", "envp", (char *)0, envp);
        return 0;
    }
    wait(NULL);
    // 无环境变量的函数使用
    if (fork() == 0)
    {
        printf("5.2 无环境变量的函数使用\n");
        execl("exec_test", "exec_test", "non-envp", (char *)0);
        return 0;
    }
    wait(NULL);

    return 0;
}
