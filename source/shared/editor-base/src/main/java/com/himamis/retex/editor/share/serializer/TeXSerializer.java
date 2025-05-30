package com.himamis.retex.editor.share.serializer;

import com.himamis.retex.editor.share.editor.SyntaxAdapter;
import com.himamis.retex.editor.share.meta.Tag;
import com.himamis.retex.editor.share.model.MathArray;
import com.himamis.retex.editor.share.model.MathCharPlaceholder;
import com.himamis.retex.editor.share.model.MathCharacter;
import com.himamis.retex.editor.share.model.MathComponent;
import com.himamis.retex.editor.share.model.MathContainer;
import com.himamis.retex.editor.share.model.MathFunction;
import com.himamis.retex.editor.share.model.MathPlaceholder;
import com.himamis.retex.editor.share.model.MathSequence;
import com.himamis.retex.editor.share.util.Unicode;

/**
 * Serializes internal format into TeX format.
 */
public class TeXSerializer extends SerializerAdapter {

	public static final int placeholderColor = 0xDCDCDC;
	public static final int commandPlaceholderColor = 0x9E9E9E;
	public static final int placeholderBackground = 0xF3F2F7;

	public static final String PLACEHOLDER = "{\\bgcolor{#"
			+ Integer.toHexString(placeholderColor) + "}\\scalebox{1}[1.6]{\\phantom{g}}}";
	private static final String cursor = "\\jlmcursor{0}";
	private static final String cursorBig = "\\jlmcursor{0.9}";
	private static final String selection_start = "\\jlmselection{";
	private static final String selection_end = "}";

	private static final String PLACEHOLDER_INVISIBLE = "\\nbsp{}";
	private boolean showPlaceholder = true;
	private boolean lineBreakEnabled = false;
	private boolean useSimplePlaceholders = false;

	private SyntaxAdapter syntaxAdapter;

	public TeXSerializer() {
		// sometimes we don't want to pass a syntax adapter
	}

	public TeXSerializer(SyntaxAdapter syntaxAdapter) {
		this.syntaxAdapter = syntaxAdapter;
	}

	@Override
	public void serialize(MathCharacter mathCharacter,
			StringBuilder stringBuilder) {
		if (mathCharacter.isUnicode(Unicode.ZERO_WIDTH_SPACE)) {
			return;
		}
		// jmathtex v0.7: incompatibility
		if (mathCharacter == currentSelStart) {
			stringBuilder.append(selection_start);
		}
		String name = mathCharacter.getUnicodeString();
		if ("=".equals(name)) {
			stringBuilder.append("\\,=\\,");
		} else if ("@".equals(name)) {
			stringBuilder.append("\\@ ");
		} else if (" ".equals(name)) {
			stringBuilder.append("\\nbsp{}");
		} else if ("\u23B8".equals(name)) {
			stringBuilder.append("\\vert{}");
		} else if (lineBreakEnabled && 10 == name.charAt(0)) {
			stringBuilder.append("\\\\\\vspace{0}");
		} else if ("n".equals(name) && stringBuilder.length() > 0
				&& 'l' == stringBuilder.charAt(stringBuilder.length() - 1)) {
			stringBuilder.setLength(Math.max(stringBuilder.length() - 1, 0));
			stringBuilder.append("\\mathrm{ln}");
		} else {
			stringBuilder.append(TeXEscaper.escapeSymbol(mathCharacter.getTexName()));
		}
		if (mathCharacter == currentSelEnd) {
			stringBuilder.append(selection_end);
		}

		// now removed - instead use \cdot{} rather than \cdot
		// safety space after operator / symbol
		// if (mathCharacter.isOperator() || mathCharacter.isSymbol()) {
		// stringBuilder.append(' ');
		// }
	}

	/**
	 * Enables or disables line break in the editor.
	 * 
	 * @param lineBreaks
	 *            whether to allow linebreaks
	 */
	public void setLineBeakEnabled(boolean lineBreaks) {
		lineBreakEnabled = lineBreaks;
	}

	/**
	 * Enables or disables placeholder in the editor.
	 *
	 * @param placeholder
	 *            whether to show placeholders
	 */
	public void setPlaceholderEnabled(boolean placeholder) {
		showPlaceholder = placeholder;
	}

	/**
	 * @return true if placeholder should be drawn
	 */
	public boolean isPlaceholderEnabled() {
		return showPlaceholder;
	}

	/**
	 * @param useSimplePlaceholders Whether to use simple placeholders in matrix
	 */
	public void useSimpleMatrixPlaceholders(boolean useSimplePlaceholders) {
		this.useSimplePlaceholders = useSimplePlaceholders;
	}

	@Override
	public void serialize(MathSequence sequence, StringBuilder stringBuilder) {
		if (sequence == null) {
			stringBuilder.append("?");
			return;
		}
		if (sequence.isScript(0) && (sequence != mCurrentField || mCurrentOffset > 0)) {
			stringBuilder.append(showPlaceholder ? PLACEHOLDER : PLACEHOLDER_INVISIBLE);
		}
		int lengthBefore = stringBuilder.length();
		boolean addBraces = (sequence.hasChildren() // {a^b_c}
				|| sequence.size() > 1 // {aa}
				|| (sequence.size() == 1 && letterLength(sequence, 0) > 1) // {\pi}
				|| (sequence.size() == 0 && sequence != mCurrentField) // {\triangleright}
				|| (sequence.size() == 1 && sequence == mCurrentField)) // {a|}
				&& (stringBuilder.length() > 0 && stringBuilder
						.charAt(stringBuilder.length() - 1) != '{');
		if (sequence == currentSelStart) {
			stringBuilder.append(selection_start);
		}
		if (addBraces) {
			// when necessary add curly braces
			stringBuilder.append('{');
		}

		if (sequence.size() == 0) {
			if (sequence == mCurrentField) {
				if (currentSelStart == null) {
					stringBuilder.append(cursorBig);
				}
			} else {
				stringBuilder.append(getPlaceholder(sequence));
			}
		} else {
			if (sequence == mCurrentField) {

				if (mCurrentOffset > 0) {
					serialize(sequence, stringBuilder, 0, mCurrentOffset);
				}
				if (currentSelStart == null) {

					stringBuilder.append(cursor);

				}
				if (mCurrentOffset < sequence.size()) {
					serialize(sequence, stringBuilder, mCurrentOffset,
							sequence.size());
				}
				boolean emptyFormula = stringBuilder
						.substring(lengthBefore, stringBuilder.length()).trim()
						.replace("\\nbsp", "").replace(cursor, "").isEmpty();
				if (emptyFormula) {
					fixCursor(stringBuilder);
				}
			} else {
				serialize(sequence, stringBuilder, 0, sequence.size());
			}
		}

		if (addBraces) {
			// when necessary add curly braces
			stringBuilder.append('}');
		}
		if (sequence == currentSelEnd) {
			stringBuilder.append(selection_end);
		}
	}

	private void fixCursor(StringBuilder stringBuilder) {
		String cursorFix = stringBuilder.toString().replace(cursor,
				cursorBig);
		stringBuilder.setLength(0);
		stringBuilder.append(cursorFix);
	}

	private String getPlaceholder(MathSequence sequence) {
		MathContainer parent = sequence.getParent();
		if (parent == null
				|| (parent instanceof MathArray && parent.size() == 1)) {
			return PLACEHOLDER_INVISIBLE;
		}
		if (parent instanceof MathFunction) {
			Tag fn = ((MathFunction) parent).getName();
			if (fn == Tag.APPLY || fn == Tag.LOG) {
				return PLACEHOLDER_INVISIBLE;
			}
		}
		return showPlaceholder ? PLACEHOLDER : PLACEHOLDER_INVISIBLE;
	}

	@Override
	public void serialize(MathFunction function, StringBuilder stringBuilder) {
		if (function == currentSelStart) {
			stringBuilder.append(selection_start);
		}
		switch (function.getName()) {

		case SUPERSCRIPT:
			appendIndex(stringBuilder, function, "^");
			break;
		case SUBSCRIPT:
			appendIndex(stringBuilder, function, "_");
			break;

		case FRAC:
			if (buildMixedNumber(stringBuilder, function)) {
				stringBuilder.replace(0, stringBuilder.length(), stringBuilder
						.toString().replace("\\nbsp{}", "")); // Remove spaces
				break;
			}
			stringBuilder.append("{");
			stringBuilder.append(function.getTexName());
			stringBuilder.append("{");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("}{");
			serialize(function.getArgument(1), stringBuilder);
			stringBuilder.append("}}");
			break;
		case SQRT:
		case CBRT:
			stringBuilder.append(function.getTexName());
			stringBuilder.append("{");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("}");
			break;
		case NROOT:
			stringBuilder.append(function.getTexName());
			stringBuilder.append('[');
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("]{");
			serialize(function.getArgument(1), stringBuilder);
			stringBuilder.append('}');
			break;
		case LOG:
			stringBuilder.append(function.getTexName());
			// only proint log base if nonempty or contains cursor
			if (function.getArgument(0).size() > 0
					|| mCurrentField == function.getArgument(0)) {
				stringBuilder.append("_{");
				serialize(function.getArgument(0), stringBuilder);
				stringBuilder.append('}');
			}
			stringBuilder.append("\\left(");
			serialize(function.getArgument(1), stringBuilder);
			stringBuilder.append("\\right)");
			break;
		case SUM_EQ:
		case PROD_EQ:
		case DEF_INT:
			stringBuilder.append(function.getTexName());
			stringBuilder.append('_');
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append('^');
			serialize(function.getArgument(1), stringBuilder);
			stringBuilder.append("{}");
			break;
		case LIM_EQ:
			stringBuilder.append("\\lim_{");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("} ");
			break;
		case ABS:
			stringBuilder.append("\\left|");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("\\right|");
			break;
		case FLOOR:
			stringBuilder.append("\\left\\lfloor ");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("\\right\\rfloor ");
			break;
		case CEIL:
			stringBuilder.append("\\left\\lceil ");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("\\right\\rceil ");
			break;
		case APPLY:
		case APPLY_SQUARE:
			StringBuilder functionName = new StringBuilder();
			serialize(function.getArgument(0), functionName);

			stringBuilder.append("{");
			if (isFunction(functionName.toString())) {
				stringBuilder.append("{\\mathrm{").append(functionName).append("}}");
			} else {
				stringBuilder.append(functionName);
			}

			serializeArguments(stringBuilder, function, 1);
			break;
		case VEC:
			stringBuilder.append("\\overrightarrow{");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("}");
			break;
		case ATOMIC_POST:
			stringBuilder.append("{");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("}_{");
			serialize(function.getArgument(1), stringBuilder);
			stringBuilder.append("}^{");
			serialize(function.getArgument(2), stringBuilder);
			stringBuilder.append("}");
			break;
		case ATOMIC_PRE:
			stringBuilder.append("\\ce{^{");
			serialize(function.getArgument(1), stringBuilder);
			stringBuilder.append("}_{");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("}");
			serialize(function.getArgument(2), stringBuilder);
			stringBuilder.append("}");
			break;
		case POINT:
			point(function, stringBuilder, ",");
			break;
		case POINT_AT:
			point(function, stringBuilder, "\\vert");
			break;
		case VECTOR:
			vector(function, stringBuilder);
			break;
		case MIXED_NUMBER:
			stringBuilder.append("{");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("}\\frac{");
			serialize(function.getArgument(1), stringBuilder);
			stringBuilder.append("}{");
			serialize(function.getArgument(2), stringBuilder);
			stringBuilder.append("}");
			break;
		case RECURRING_DECIMAL:
			stringBuilder.append("\\overline{");
			serialize(function.getArgument(0), stringBuilder);
			stringBuilder.append("}");
			MathComponent next = function.nextSibling();
			if (!(next instanceof MathCharacter) || !((MathCharacter) next).isWordBreak()) {
				stringBuilder.append("\\nbsp{}");
			}
			break;
		default:
			stringBuilder.append("{\\mathrm{");
			stringBuilder.append(function.getTexName());
			stringBuilder.append("}");
			serializeArguments(stringBuilder, function, 0);

		}
		if (function == currentSelEnd) {
			stringBuilder.append(selection_end);
		}
	}

	private void point(MathFunction function, StringBuilder stringBuilder, String s) {
		stringBuilder.append("\\left(\\jlminput{");
		serialize(function.getArgument(0), stringBuilder);
		for (int i = 1; i < function.size(); i++) {
			stringBuilder.append("}").append(s).append("\\jlminput{");
			serialize(function.getArgument(i), stringBuilder);
		}
		stringBuilder.append("}\\right)");
	}

	private void vector(MathFunction function, StringBuilder stringBuilder) {
		stringBuilder.append("\\begin{pmatrix}");
		for (int i = 0; i < function.size(); i++) {
			stringBuilder.append("\\jlminput{");
			serialize(function.getArgument(i), stringBuilder);
			stringBuilder.append("}\\\\");
		}
		stringBuilder.append("\\end{pmatrix}");
	}

	/**
	 * @param name function name
	 * @return whether this is a builtin function (sin/cos/...)
	 */
	public boolean isFunction(String name) {
		if (syntaxAdapter == null) {
			return true;
		}

		String trimmed = name.replace(cursor, "");
		if (trimmed.indexOf('^') > 0) {
			trimmed = trimmed.substring(0, trimmed.indexOf('^'));
		}

		return syntaxAdapter.isFunction(trimmed);
	}

	private void serializeArguments(StringBuilder stringBuilder,
			MathFunction function, int offset) {
		stringBuilder.append("\\left");
		stringBuilder.append(function.getOpeningBracket());
		for (int i = offset; i < function.size(); i++) {
			serialize(function.getArgument(i), stringBuilder);
			if (i + 1 < function.size()) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("\\right");
		stringBuilder.append(function.getClosingBracket());
		stringBuilder.append("}");
	}

	private void appendIndex(StringBuilder stringBuilder, MathFunction function,
			String idxType) {
		MathSequence parent = function.getParent();
		int index = function.getParentIndex();
		if (index == 0
				|| (index > 0
						&& parent
								.getArgument(index - 1) instanceof MathCharacter
						&& ((MathCharacter) parent.getArgument(index - 1))
								.isOperator())) {
			stringBuilder.append(PLACEHOLDER_INVISIBLE);
		}
		stringBuilder.append(idxType);
		stringBuilder.append('{');
		serialize(function.getArgument(0), stringBuilder);
		stringBuilder.append('}');

	}

	@Override
	public void serialize(MathArray array, StringBuilder stringBuilder) {
		boolean showFancyPlaceholders = array.isMatrix() && !useSimplePlaceholders;
		if (this.currentSelStart == array) {
			stringBuilder.append(TeXSerializer.selection_start);
		}
		stringBuilder.append(array.getOpen().getTexName());
		for (int i = 0; i < array.rows(); i++) {
			for (int j = 0; j < array.columns(); j++) {
				stringBuilder.append(showFancyPlaceholders ? "\\jlminput{" : "");
				serialize(array.getArgument(i, j), stringBuilder);
				stringBuilder.append(showFancyPlaceholders ? "}" : "");
				if (j + 1 < array.columns()) {
					stringBuilder.append(array.getField().getTexName());
				} else if (i + 1 < array.rows()) {
					stringBuilder.append(array.getRow().getTexName());
				}
			}
		}
		stringBuilder.append(array.getClose().getTexName());
		if (this.currentSelEnd == array) {
			stringBuilder.append(TeXSerializer.selection_end);
		}
	}

	@Override
	void serialize(MathPlaceholder placeholder, StringBuilder stringBuilder) {
		stringBuilder.append("{\\color{#")
				.append(Integer.toHexString(commandPlaceholderColor))
				.append("}")
				.append(placeholder.getContent())
				.append("}");
	}

	@Override
	void serialize(MathCharPlaceholder placeholder, StringBuilder stringBuilder) {
		stringBuilder.append(PLACEHOLDER);
	}

	private static int letterLength(MathSequence symbol, int i) {
		if (symbol.getArgument(i) instanceof MathCharacter) {
			return ((MathCharacter) symbol.getArgument(i)).getTexName()
					.length();
		}
		return 2;
	}

	/**
	 * @param stringBuilder StringBuilder
	 * @param mathFunction MathFunction
	 * @return True if a mixed number was built
	 */
	@Override
	public boolean buildMixedNumber(StringBuilder stringBuilder, MathFunction mathFunction) {
		//Check if a valid mixed number can be created (e.g.: no 'x')
		if (isMixedNumber(stringBuilder) < 0 || !isValidMixedNumber(mathFunction)) {
			return false;
		}

		stringBuilder.insert(isMixedNumber(stringBuilder), "{");
		stringBuilder.append("}\\frac{");
		serialize(mathFunction.getArgument(0), stringBuilder);
		stringBuilder.append("}{");
		serialize(mathFunction.getArgument(1), stringBuilder);
		stringBuilder.append("}");
		return true;
	}

	/**
	 * Checks if the stringBuilder contains a mixed number e.g. 3 1/2 <br>
	 * @param stringBuilder StringBuilder
	 * @return Index &gt;= 0 of where to put opening parentheses if there is a mixed number, -1 else
	 */
	@Override
	public int isMixedNumber(StringBuilder stringBuilder) {
		boolean isMixedNumber = false;
		for (int i = stringBuilder.length() - 1; i >= 0; i--) {
			if (i >= 6 && stringBuilder.substring(i - 6, i + 1).equals("\\nbsp{}")
					&& !isMixedNumber) {
				i -= 6; // Expecting a space preceding the fraction
				continue;
			} else if (Character.isDigit(stringBuilder.charAt(i))) {
				isMixedNumber = true; // Only allow digits here
			} else if (isMixedNumber
					&& " +-*/(}{".contains(Character.toString(stringBuilder.charAt(i)))) {
				return i + 1;
			} else {
				isMixedNumber = false;
				break;
			}
		}
		return isMixedNumber ? 0 : -1;
	}

	/**
	 * @param ms
	 *            sequence
	 * @return TeX representation of the sequence
	 */
	public static String serialize(MathSequence ms) {
		StringBuilder b = new StringBuilder();
		new TeXSerializer().serialize(ms, b);
		return b.toString();
	}

	public void setSyntaxAdapter(SyntaxAdapter syntaxAdapter) {
		this.syntaxAdapter = syntaxAdapter;
	}
}
