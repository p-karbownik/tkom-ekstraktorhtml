package Lexer;

public enum TokenType {
    RESOURCE, // resource
    TAG, // tag
    CONDITIONS, // conditions
    IF, // if
    PARENT, // parent
    CHILD, // child
    HAS, // has
    CLASS, // class
    ATTRIBUTE, // attribute
    OR, // or
    AND, // and
    EXPORT, // export
    TO, // to
    SET, // set
    FIELDS, //fields
    TEXT, // text
    IMG, // img
    EVERY, // every
    FROM, // from
    THIS, // this
    AMOUNT, // amount
    NO, // no
    FIELD, // field
    LEFT_ROUND_BRACKET,
    RIGHT_ROUND_BRACKET,
    LEFT_BRACE, // {
    RIGHT_BRACE, // }
    EQUAL, // ==
    NOT_EQUAL, // !=
    ASSIGN_OPERATOR, // =
    DOT, // .
    SEMI_COLON, // ;
    NUMBER, // 1234567890
    STRING // String
}
