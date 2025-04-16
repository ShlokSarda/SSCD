#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

void E();
void EP();
void T();
void TP();
void F();
void advance();

int i = 0;
char str[100], tp[10];

void advance() {
    while (str[i] == ' ') i++; // Skip spaces

    if (str[i] == '\0') {
        strcpy(tp, "\0");
        return;
    }

    // Handle 'id'
    if (str[i] == 'i' && str[i+1] == 'd') {
        strcpy(tp, "id");
        i += 2;
    }
    // Handle single character tokens like +, *, (, )
    else {
        tp[0] = str[i];
        tp[1] = '\0';
        i++;
    }
}

void F() {
    if (strcmp(tp, "id") == 0) {
        advance();
    } else if (strcmp(tp, "(") == 0) {
        advance();
        E();
        if (strcmp(tp, ")") == 0) {
            advance();
        } else {
            printf("String not Accepted (Missing closing parenthesis)\n");
            exit(1);
        }
    } else {
        printf("String not Accepted (Expected id or '('\n)");
        exit(1);
    }
}

void TP() {
    if (strcmp(tp, "+") == 0) {
        advance();
        F();
        TP();
    }
}

void T() {
    F();
    TP();
}

void EP() {
    if (strcmp(tp, "*") == 0) {
        advance();
        T();
        EP();
    }
}

void E() {
    T();
    EP();
}

int main() {
    printf("Enter the String: ");
    fgets(str, sizeof(str), stdin);
    str[strcspn(str, "\n")] = '\0'; // Remove newline

    i = 0;
    advance(); // Initialize first token

    E(); // Start parsing

    if (strcmp(tp, "\0") == 0)
        printf("String Accepted\n");
    else
        printf("String not Accepted (Extra symbols at the end)\n");

	return 0;
}