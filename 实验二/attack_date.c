#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main()
{
    while(1)
    {
        sleep(1);
        pid_t pid;
        if((pid = fork()) == 0)
        {
            execlp("date", "date", "-s", "2020-11-28", (int *)0);
            return 0;
        } 
        wait(NULL);
    }
}
