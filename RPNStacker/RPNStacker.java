package rpnstacker;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.util.Map;
import java.util.Stack;
import java.util.Scanner;
import java.util.HashMap;
import java.util.function.DoubleBinaryOperator;

import rpnstacker.lexer.token;
import rpnstacker.lexer.regex;

public class RPNStacker {
    public static Stack <Double> stack = new Stack<Double>();

    public static Map <String, DoubleBinaryOperator> exp = new HashMap<>();

    // Added this with the same structure used for the operators, but for the tokens
    public static Map <String, Token> tokens = new HashMap<>();
    
    public static Scanner input = new Scanner(System.in);

    public static String[] split() { return input.nextLine().split(" \n"); }
    
    // Simple code to rule out if it is a signal
    public static boolean signalDecider(String s) {
        return (
            s.equals("-") || 
            s.equals("*") ||
            s.equals("+") ||
            s.equals("/")
        );
    } 

    // Reading file and handling exceptions
    public static String readFile (String filepath) throws IOException, FileNotFoundException {
        BufferedReader buffer = new BufferedReader(new FileReader(filepath));
        String everything;
        try {
            String line = buffer.readLine();
            StringBuilder builder = new StringBuilder();
            
            while (line != null) {
                line = buffer.readLine();
                builder.append(line + " ");
            }
            everything = builder.toString();
        } finally {
            buffer.close();
        }
        return everything;
    }
    
    // Transforms Strings in lists of terms and calls evaluate to resolve
    public static Double operate (String expression) {
        return evaluate(expression.split(" "));
    }

    // Sets up the base expression. Added the tokens here too
    public static void setUp() {
        exp.put("+", (a, b) -> a + b);
        exp.put("-", (a, b) -> a - b);
        exp.put("/", (a, b) -> a / b);
        exp.put("*", (a, b) -> a * b);

        tokens.put("*", new Token(TokenType.STAR, "*"));
        tokens.put("/", new Token(TokenType.SLASH, "/"));
        tokens.put("+", new Token(TokenType.PLUS, "+"));
        tokens.put("-", new Token(TokenType.MINUS, "-"));
    }

    // Resolves the expression on the list of terms
    // That's where most of the things changed from task1
    // That's where most of the things changed from task2 aswell
    // It's really similar to the other version, but using Regex now
    public static Double evaluate(String[] expression) {
        for (String s: expression) {
            if (Regex.checkNUM(s)) {
                try {
                    stack.push(Double.parseDouble(s));
                    System.out.println(new Token(TokenType.NUM, s.toString()).toString());
                } catch (Exception e) {
                    throw new Error("Unexpected character: " + s);
                }
            }
            else if (Regex.checkOP(s)){
                Double a = stack.pop();
                Double b = stack.pop();
                try {
                    Double result = exp.get(s).applyAsDouble(b, a);
                    stack.push(result);
                    System.out.println(tokens.get(s).toString());
                } catch (Exception e) {
                    throw new Error("Unexpected character: " + s);
                }
            }
            else {
                throw new Error("Unexpected character: " + s);
            }
        }
        System.out.println(new Token(TokenType.EOF, "EOF").toString());

        double res = stack.pop();

        if (stack.empty()) return res;

        else throw new Error("Invalid equation");
    }

    public static void fromFile (String filepath) {
        try {
            String exp = readFile(filepath);
            System.out.println(operate(exp));
        } catch (Exception notFound) {
            System.out.println("Error: " + notFound);    
        }
    }

    public static void main(String[] args) {
        setUp();

        fromFile("Calc1.stk");
        
        input.close();
    }
}