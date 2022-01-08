#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

int main()
{
    uid_t ruid, euid, suid;
    getresuid(&ruid, &euid, &suid);
    printf("进入chroot前 ：ruid:%d euid:%d suid:%d\n", ruid, euid, suid);
    if(chroot("/home/cdx1190201308/CCS")<0){
        printf("chroot error\n");
        return 0;
    }
    if (setresuid(1000, 1000, 1000) < 0)
    {
        printf("setuid函数出错\n");
    }
    getresuid(&ruid, &euid, &suid);
    printf("进入chroot后 ：ruid:%d euid:%d suid:%d\n", ruid, euid, suid);
}