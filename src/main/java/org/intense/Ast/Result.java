package org.intense.Ast;

public record Result<T>(T value, Exception error) {
    public static <T> Result<T> ok(T value) { return new Result<>(value, null); }
    public static <T> Result<T> error(Exception e) { return new Result<>(null, e); }

    public boolean isSuccess() { return error == null; }
}

