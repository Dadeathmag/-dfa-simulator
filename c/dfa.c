#include<stdio.h>
#include<string.h>


enum type{
    normal,//0 for normal state
    dead,//1 for dead state
    final//2 for final state
};

int States[10];//defines type of states in dfa 
char Alpha[10];//Alphabets accepted by dfa
int TransitionTable[10][10];

void transitionTable(int *noOfStates,int *noOfAlphabets){
    
    printf("Enter number of states:");
    scanf("%d",noOfStates);
    printf("\nEnter type of states\n0-for normal\n1-for dead\n2-for final\n");
    for(int i=0;i<(*noOfStates);i++){
        printf("q%d:",i);
        scanf("%d",&States[i]);
    }

    printf("Enter number of alphabets:");
    scanf("%d",noOfAlphabets);
    printf("Enter alphabets:");
    for(int j=0;j<(*noOfAlphabets);j++){
        scanf(" %c",&Alpha[j]);
    }

    printf("\nFill Transition Table\n\n  ");
    for(int j=0;j<(*noOfAlphabets);j++){
        printf(" %c",Alpha[j]);
    }
    printf("\n");

    for(int i=0;i<(*noOfStates);i++){
        printf("q%d:",i);
        for(int j=0;j<(*noOfAlphabets);j++){
            scanf("%d",&TransitionTable[i][j]);
        }
    }
}

int dfa(char String[],int noOfStates,int noOfAlphabets){
    int len=strlen(String);
    int i=0,j,count=0,found;
    while(count<len){
        //string checking
        for(j=0;j<noOfAlphabets;j++){
            found=0;
            if(String[count]==Alpha[j]){
                found=1;
                goto next;
            }
        }
        next:
        if(!found){
            printf("Alphabet not recognised\n");
            return 0;
        }
        else{
            i=TransitionTable[i][j];
        }
        count++;
    }
    if(States[i]==final){
        printf("String accepted\n");
        return 1;
    }else{
        printf("String not accepted\n");
        return 0;
    }
}


void main(){
    int noOfStates,noOfAlphabets;
    char String[20];
    int choice;
    transitionTable(&noOfStates,&noOfAlphabets);
    do{
        printf("\nEnter the String to test:");
        scanf("%s",String);
        dfa(String,noOfStates,noOfAlphabets);
        printf("Enter 1 to try another\nEnter 0 to exit\nchoice:");
        scanf("%d",&choice);
    }while(choice);
}