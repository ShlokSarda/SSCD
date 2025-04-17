%{
#include <stdio.h>
extern FILE *yyin;
int line_no = 1;
%}

%option noyywrap

DIGIT       [0-9]
ID          [a-zA-Z_][a-zA-Z0-9_]*
KEYWORD     int|float|if|else|while|return|void|char|for|do|break|continue
OPERATOR    \+|\-|\*|\/|=|==|!=|<=|>=|<|>
DELIMITER   [;,\(\)\{\}\[\]]
WHITESPACE  [ \t]+
NEWLINE     \n

%%

{KEYWORD}       { printf("Keyword: %s\n", yytext); }
{ID}            { printf("Identifier: %s\n", yytext); }
{DIGIT}+        { printf("Number: %s\n", yytext); }
{OPERATOR}      { printf("Operator: %s\n", yytext); }
{DELIMITER}     { printf("Delimiter: %s\n", yytext); }
{NEWLINE}       { line_no++; }
{WHITESPACE}    { /* skip whitespace */ }
.               { printf("Unknown character: %s at line %d\n", yytext, line_no); }

%%

int main() {
    FILE *file = fopen("main.c", "r"); // 🔁 Change file name if needed
    if (!file) {
        perror("Failed to open file");
        return 1;
    }

    yyin = file;
    printf("Starting lexical analysis of 'main.c'...\n");
    yylex();
    fclose(file);
    printf("Lexical analysis completed.\n");
    return 0;
}