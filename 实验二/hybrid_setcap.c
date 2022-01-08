#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <string.h>
#include <sys/wait.h>

int main(int argc, char *argv[])
{
    // 调用bind绑定80端口需要设置 setuid 位, 否则会失败
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
    // 尝试绑定
    int ret = bind(socket_fd, (struct sockaddr *)&server_sockaddr, sizeof(server_sockaddr));
    if (ret < 0)
    {
        printf("bind error \n");
    }
    else
    {
        printf("bind success \n");
    }
    return 0;
}