#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    uid_t ruid = atoi(argv[1]), euid = atoi(argv[2]), suid = atoi(argv[3]);
    getresuid(&ruid, &euid, &suid);
    printf("函数内调用execl执行setuid前 : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
    ruid = atoi(argv[1]); euid = atoi(argv[2]); suid = atoi(argv[3]);
    if (setresuid(ruid, euid, suid) < 0)
    {
        printf("setuid函数出错\n");
    }
    getresuid(&ruid, &euid, &suid);
    printf("函数内调用execl执行setuid后 : ruid = %d, euid = %d, suid = %d\n", ruid, euid, suid);
    return 0;
}