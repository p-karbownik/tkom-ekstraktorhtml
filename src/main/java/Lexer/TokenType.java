package Lexer;

public enum TokenType {
    //tokens for my language
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
    LEFT_ROUND_BRACKET, // (
    RIGHT_ROUND_BRACKET, // )
    LEFT_BRACE, // {
    RIGHT_BRACE, // }
    EQUAL, // ==
    NOT_EQUAL, // !=
    ASSIGN_OPERATOR, // =, this type is used in HTML too
    DOT, // .
    SEMI_COLON, // ;
    NUMBER, // 1234567890
    STRING, // String

    //tokens for HTML
    CLOSING_TAG,
    DOCTYPE,
    EMPTY_CLOSING_TAG,
    QUOTE,
    TAG_CLOSING_MARK,
    TAG_OPENER,
    HTML_TEXT,
    EOF
}
