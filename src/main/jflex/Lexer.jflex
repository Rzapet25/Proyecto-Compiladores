package org.example.lexer;

import java_cup.runtime.*;
import org.example.parser.sym;

%%
%public
%class Lexer
%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

// Espacios en blanco y comentarios (a ignorar)
LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Comment        = "//" [^\r\n]* {LineTerminator}?

// Identificadores y literales
Identifier = [a-zA-Z_][a-zA-Z0-9_]*
Integer    = [0-9]+
Float      = [0-9]+ "." [0-9]+
String     = \" [^\"]* \"

%%

/* Palabras clave */
"DEFINE"       { return symbol(sym.DEFINE); }
"PRINT"        { return symbol(sym.PRINT); }
"IF"           { return symbol(sym.IF); }
"ELSE"         { return symbol(sym.ELSE); }
"ELSEIF"       { return symbol(sym.ELSEIF); }
"WHILE"        { return symbol(sym.WHILE); }
"LOOP"         { return symbol(sym.LOOP); }
"FUNCTION"     { return symbol(sym.FUNCTION); }
"RETURN"       { return symbol(sym.RETURN); }
"END"          { return symbol(sym.END); }
"DO"           { return symbol(sym.DO); }
"THEN"         { return symbol(sym.THEN); }
"AND"          { return symbol(sym.AND); }
"OR"           { return symbol(sym.OR); }
"NOT"          { return symbol(sym.NOT); }
"true"         { return symbol(sym.TRUE, true); }
"false"        { return symbol(sym.FALSE, false); }

/* Operadores */
"+"            { return symbol(sym.PLUS); }
"-"            { return symbol(sym.MINUS); }
"*"            { return symbol(sym.TIMES); }
"/"            { return symbol(sym.DIVIDE); }
"<"            { return symbol(sym.LT); }
">"            { return symbol(sym.GT); }
"<="           { return symbol(sym.LE); }
">="           { return symbol(sym.GE); }
"=="           { return symbol(sym.EQ); }
"!="           { return symbol(sym.NE); }
"="            { return symbol(sym.ASSIGN); }

/* Delimitadores */
"("            { return symbol(sym.LPAREN); }
")"            { return symbol(sym.RPAREN); }
";"            { return symbol(sym.SEMICOLON); }
","            { return symbol(sym.COMMA); }

/* Identificadores y literales */
{Identifier}   { return symbol(sym.ID, yytext()); }
{Integer}      { return symbol(sym.INT, Integer.parseInt(yytext())); }
{Float}        { return symbol(sym.FLOAT, Float.parseFloat(yytext())); }
{String}       { return symbol(sym.STRING, yytext().substring(1, yytext().length() - 1)); }

/* Espacios en blanco y comentarios */
{WhiteSpace}   { /* ignorar */ }
{Comment}      { /* ignorar */ }

/* Error */
[^]            { throw new Error("Caracter ilegal <" + yytext() + "> en línea " + (yyline + 1) + ", columna " + (yycolumn + 1)); }