/**
 * Preprocesa una expresión regular antes de pasarla al parser.
 *
 * El método directo solo trabaja con: |, ·, *, ε
 * Por eso +  →  r·r*   y   ?  →  (r|ε)
 */

public class RegexPreprocessor {

    /**
     * Punto de entrada. Recibe la regex del usuario y retorna la
     * expresión aumentada, expandida, con · explícito y # al final.
     */
    public String preprocess(String regex) {
        String expanded   = expandOperators(regex);
        String withConcat = insertConcatOperator(expanded);
        return withConcat + "·#";
    }

    
    // Paso 1: Expande + y ? en términos de |, ·, *

     /**
     * Recorre la expresión de derecha a izquierda buscando + y ?.
     * Para cada uno, identifica el "átomo" que lo precede y lo reescribe.
     *
     * Un átomo puede ser:
     *   - un carácter simple: a+ → aa*
     *   - un grupo entre paréntesis: (ab)+ → (ab)(ab)*
     */

    private String expandOperators(String regex) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        while (i < regex.length()) {
            char c = regex.charAt(i);

            if (i + 1 < regex.length() && isPostfixOp(regex.charAt(i + 1))) {
                char op = regex.charAt(i + 1);

                if (c == ')') {
                    int closePos = result.length();
                    result.append(c);
                    int groupStart = findMatchingOpen(result.toString(), result.length() - 1);

                    String group = result.substring(groupStart);

                    if (op == '+') {
                        // (ab)+ → (ab)(ab)*
                        result.append(group).append("*");
                    } else {
                        // (ab)? → ((ab)|ε)
                        result = new StringBuilder(result.substring(0, groupStart));
                        result.append("(").append(group).append("|ε)");
                    }
                } else {
                    // El átomo es un carácter simple
                    if (op == '+') {
                        // a+ → aa*
                        result.append(c).append(c).append("*");
                    } else {
                        // a? → (a|ε)
                        result.append("(").append(c).append("|ε)");
                    }
                }
                i += 2; // consumimos el carácter + el operador

            } else {
                result.append(c);
                i++;
            }
        }

        return result.toString();
    }

    /**
     * Encuentra la posición del paréntesis abierto '(' que corresponde
     * al paréntesis cerrado ')' en la posición closeIndex dentro de str.
     */
    private int findMatchingOpen(String str, int closeIndex) {
        int depth = 0;
        for (int i = closeIndex; i >= 0; i--) {
            if (str.charAt(i) == ')') depth++;
            else if (str.charAt(i) == '(') {
                depth--;
                if (depth == 0) return i;
            }
        }
        throw new IllegalArgumentException("Paréntesis desbalanceados en: " + str);
    }

    private boolean isPostfixOp(char c) {
        return c == '+' || c == '?';
    }

    // Paso 2: Inserta el operador de concatenación explícito (·)
    /**
     * Inserta el carácter '·' entre tokens que se concatenan implícitamente.
     * Reglas para insertar '·':
     *  - Entre un símbolo o grupo y otro símbolo o grupo: a(b) → a·(b)
     * - Entre un símbolo o grupo y un operador de cierre: a* b → a*·b
     * - Entre un operador de cierre y un símbolo o grupo: (a) b → (a)·b
     * - Entre un operador de cierre y otro operador de cierre: (a)* (b) → (a)*·(b)
     */

    private String insertConcatOperator(String regex) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < regex.length() - 1; i++) {
            char curr = regex.charAt(i);
            char next = regex.charAt(i + 1);
            result.append(curr);

            if (shouldInsertConcat(curr, next)) {
                result.append('·');
            }
        }

        // Agregar el último carácter
        result.append(regex.charAt(regex.length() - 1));
        return result.toString();
    }

    // Decide si se debe insertar '·' entre el caracter izquierdo y el derecho
    private boolean shouldInsertConcat(char left, char right) {
        boolean leftIsOperand  = isOperand(left) || left == '*' || left == ')';
        boolean rightIsOperand = isOperand(right) || right == '(';
        return leftIsOperand && rightIsOperand;
    }

    /**
     * Un "operando" es cualquier carácter que no sea operador ni paréntesis.
     * Incluye letras, dígitos, #, ε.
     */
    private boolean isOperand(char c) {
        return c != '|' && c != '*' && c != '·' && c != '(' && c != ')' && c != 'ε';
    }


}
