%{
#include<stdio.h>
extern int yylex();
extern int yywrap();
extern int yyparse();
extern int yyerror(char *str);
%}

%token WH OP CP CMP OPR ASG ID NUM SC
%%

start: swhile;
swhile: WH OP cndn CP stmt      {printf("VALID STATEMENT WHILE\n");};
cndn: ID CMP ID | ID CMP NUM;
stmt:	ID ASG ID OPR ID SC | ID ASG ID OPR NUM SC | ID ASG NUM OPR ID SC | ID ASG NUM OPR NUM SC | ID ASG ID SC | ID ASG NUM SC;
%%

int yyerror(char *str)
{
	printf("%s", str);
}

int main()
{
	yyparse();
	return 1;
}