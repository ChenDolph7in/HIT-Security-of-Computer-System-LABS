#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pwd.h>
#include <sys/types.h>
#include <sys/stat.h>

void changePassword(const char *username, const char *password)
{
    printf("更改 %s 的密码\n", username);
    // 打开待修改文件
    FILE *fp = NULL;
    fp = fopen("aaa", "r+");
    if (fp == NULL)
    {
        perror("fopen\n");
    }
    // 逐行读取文件内容
    size_t len = 0;          // 文件总长度
    ssize_t lineLen;         // 当前行长度
    char *lineStr = NULL;    // 当前行字符串
    long offset = ftell(fp); // 初始化文件当前偏移位置
    while ((lineLen = getline(&lineStr, &len, fp)) != -1)
    {
        if (strlen(lineStr) == 0)
        {
            continue;
        }
        // 截取每行空格前用户名部分
        char *lineUsername = strsep(&lineStr, " ");
        // 若匹配，则修改空格后面的密码，剩下行不变
        if (strcmp(username, lineUsername) == 0)
        {
            char buffer[1024] = {0};
            int c = 0;
            // 缓存匹配行后面的部分
            while (!feof(fp))
            {
                buffer[c] = fgetc(fp);
                c++;
            }
            if (c != 0)
            {
                buffer[c - 1] = '\0';
            }
            // 从文件开头查找回到当前行后偏移位置
            fseek(fp, offset, SEEK_SET);
            fprintf(fp, "%s %s\n", username, password);
            // 向文件写缓存的后面的行
            if (c != 0)
            {
                fprintf(fp, "%s", buffer);
            }
            fclose(fp);
            return;
        }
        offset = ftell(fp); // 更新文件当前偏移，方便只修改指针后边的部分
    }
    printf("passwd: 用户 %s 不存在\n", username);
    return;
}

int main(int argc, char const *argv[])
{
    // 获得实际用户ID，有效用户ID，保存的用户ID
    uid_t ruid, euid, suid;
    getresuid(&ruid, &euid, &suid);
    // 根据实际用户uid获得passwd结构体，从而获得实际用户的用户
    struct passwd *password = getpwuid(ruid);
    char *ruid_name = password->pw_name;
    // 再根据选项数输出被更改密码的账户名
    if (argc == 2)
    {
        printf("%s ", ruid_name); // 先输出更改密码的用户名
        changePassword(ruid_name, argv[1]);
    }
    else if (argc == 3)
    {
        if (strcmp(ruid_name, argv[1]) == 0)
        {
            printf("%s ", ruid_name);
            changePassword(argv[1], argv[2]);
        }
        // 只有root用户能更改其他用户的密码
        else if (strcmp("root", ruid_name) == 0)
        {
            printf("%s ", ruid_name);
            changePassword(argv[1], argv[2]);
        }
        else
        {
            printf("%s 权限不足\n", ruid_name);
        }
    }
    else
    {
        printf("用法：user_passwd [用户名](默认调用用户) [密码]\n");
    }
}
