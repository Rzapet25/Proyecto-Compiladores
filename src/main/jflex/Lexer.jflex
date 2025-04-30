package org.example.lexer;

import java_cup.runtime.*;
import org.example.parser.Symbols;

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
"DEFINE"       { return symbol(Symbols.DEFINE); }
"PRINT"        { return symbol(Symbols.PRINT); }
"IF"           { return symbol(Symbols.IF); }
"ELSE"         { return symbol(Symbols.ELSE); }
"ELSEIF"       { return symbol(Symbols.ELSEIF); }
"WHILE"        { return symbol(Symbols.WHILE); }
"LOOP"         { return symbol(Symbols.LOOP); }
"FUNCTION"     { return symbol(Symbols.FUNCTION); }
"RETURN"       { return symbol(Symbols.RETURN); }
"END"          { return symbol(Symbols.END); }
"DO"           { return symbol(Symbols.DO); }
"THEN"         { return symbol(Symbols.THEN); }
"AND"          { return symbol(Symbols.AND); }
"OR"           { return symbol(Symbols.OR); }
"NOT"          { return symbol(Symbols.NOT); }
"true"         { return symbol(Symbols.TRUE, true); }
"false"        { return symbol(Symbols.FALSE, false); }

/* Operadores */
"+"            { return symbol(Symbols.PLUS); }
"-"            { return symbol(Symbols.MINUS); }
"*"            { return symbol(Symbols.TIMES); }
"/"            { return symbol(Symbols.DIVIDE); }
"<"            { return symbol(Symbols.LT); }
">"            { return symbol(Symbols.GT); }
"<="           { return symbol(Symbols.LE); }
">="           { return symbol(Symbols.GE); }
"=="           { return symbol(Symbols.EQ); }
"!="           { return symbol(Symbols.NE); }
"="            { return symbol(Symbols.ASSIGN); }

/* Delimitadores */
"("            { return symbol(Symbols.LPAREN); }
")"            { return symbol(Symbols.RPAREN); }
";"            { return symbol(Symbols.SEMICOLON); }
","            { return symbol(Symbols.COMMA); }

/* Identificadores y literales */
{Identifier}   { return symbol(Symbols.ID, yytext()); }
{Integer}      { return symbol(Symbols.INT, Integer.parseInt(yytext())); }
{Float}        { return symbol(Symbols.FLOAT, Float.parseFloat(yytext())); }
{String}       { return symbol(Symbols.STRING, yytext().substring(1, yytext().length() - 1)); }

/* Espacios en blanco y comentarios */
{WhiteSpace}   { /* ignorar */ }
{Comment}      { /* ignorar */ }

/* Error */
[^]            { throw new Error("Caracter ilegal <" + yytext() + "> en línea " + (yyline + 1) + ", columna " + (yycolumn + 1)); }