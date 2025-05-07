package org.intense;

class Token(var type: TokenType,var value: String, var line: Int, var column: Int) {

    override fun toString() :String {
        return String.format("Token(%s, \"%s\", line=%d, col=%d)",
                type, value, line, column);
    }
}
