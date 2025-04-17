%{
    extern FILE *yyin;
%}

DIGIT       [0-9]
ID          [a-zA-Z_][a-zA-Z0-9_]*
OPERATOR    "+"|"-"|"/"|"*"
ASG         "="
OP          "("
CP          ")"

%%

print       {printf("Print: %s\n",yytext);};
input       {printf("Input: %s\n",yytext);};
{DIGIT}+    {printf("Digit: %s\n",yytext);};
{ID}        {printf("Identifier: %s\n",yytext);};
{OPERATOR}  {printf("Operator: %s\n",yytext);};
{ASG}       {printf("Assignment Variable: %s\n",yytext);};
{OP}        {printf("OP: %s\n",yytext);};
{CP}        {printf("CP: %s\n",yytext);};


%%

int main() {
    FILE *file = fopen("main.py", "r"); // 🔁 Change file name if needed
    if (!file) {
        perror("Failed to open file");
        return 1;
    }

    yyin = file;
    printf("Starting lexical analysis of 'main.py'...\n");
    yylex();
    fclose(file);
    printf("Lexical analysis completed.\n");
    return 0;
}

int yywrap(){
    return 1;
}