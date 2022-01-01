package Lexer;

public enum TokenType {
    //tokens for my language
    RESOURCE, // resource
    TAG, // tag
    CONDITIONS, // conditions
    IF, // if
    PARENT, // parent
    CHILD, // child
    DESCENDANT,
    ANCESTOR,
    HAS, // has
    CLASS, // class
    ATTRIBUTE, // attribute
    OR, // or
    AND, // and
    EXPORT, // export
    TO, // to
    SET, // set
    FIELDS, //fields
    ASIMG, // asImg
    EVERY, // every
    FROM, // from
    AMOUNT, // amount
    NOT, // not
    FIELD, // field
    LEFT_ROUND_BRACKET, // (
    RIGHT_ROUND_BRACKET, // )
    LEFT_SQUARE_BRACKET,
    RIGHT_SQUARE_BRACKET,
    LEFT_BRACE, // {
    RIGHT_BRACE, // }
    EQUAL, // ==
    NOT_EQUAL, // !=
    ASSIGN_OPERATOR, // =, this type is used in HTML too
    DOT, // .
    COMMA, // ,
    SEMI_COLON, // ;
    NUMBER, // 1234567890
    STRING, // "String"
    IDENTIFIER,
    SELF,
    RANGE,

    //tokens for HTML
    CLOSING_TAG,
    DOCTYPE, //<!
    EMPTY_CLOSING_TAG, // />
    SINGLE_QUOTE,
    QUOTE, // "
    TAG_CLOSING_MARK, // >
    TAG_OPENER, // <
    COMMENT_TAG_OPENER, // <!--
    COMMENT_TAG_CLOSING, // -->
    HTML_TEXT,
    ETX
}
