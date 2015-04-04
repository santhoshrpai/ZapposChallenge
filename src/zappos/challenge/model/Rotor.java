package zappos.challenge.model;

public class Rotor {

	private char position;
	private char notch;
	public static final int MAX = 94;

	protected char[] staticMessage = new char[MAX];
	private char[] scrambledMessage = new char[MAX];

	public char[] getScramb() {
		return scrambledMessage;
	}

	public void setScramb(char[] scramb) {
		this.scrambledMessage = scramb;
	}

	public Rotor(String mapping, String staticWheel) {
		staticMessage = staticWheel.toCharArray();
		for (int i = 0; i < mapping.length(); i++) {
			scrambledMessage[i] = mapping.charAt(i);
		}
		position = 0;
	}

	protected char getStartPosition() {
		return position;
	}

	public void set(char character) {
		position = character;
	}

	public char getNotch() {
		return notch;
	}

	public void setNotch(char notch) {
		this.notch = notch;
	}

	public char decodeLR(char character) {
		int index = getIndex(staticMessage, character);
		// return staticToScrambler.get(character);
		return scrambledMessage[index];
	}

	public char decodeRL(char character) {
		// return scramblerToStatic.get(character);
		int index = getIndex(scrambledMessage, character);
		// return staticToScrambler.get(character);
		return staticMessage[index];
	}

	public boolean checkNotchAndStep() {
		return true;
	}

	public void rotate(boolean stepByOne) {
		int rotateAtIndex = 93;
		if (!stepByOne) {
			rotateAtIndex = getIndex(scrambledMessage, getStartPosition());
		}
		int offset = rotateAtIndex;
		char[] copy = new char[94];
		for (int i = 0; i < scrambledMessage.length; i++) {
			copy[i] = scrambledMessage[i];
		}
		if (offset > 0) {
			for (int i = 0; i < scrambledMessage.length; i++) {
				int j = (i + offset) % scrambledMessage.length;
				scrambledMessage[i] = copy[j];
			}
		}
		set(scrambledMessage[0]);
	}

	public int getIndex(char[] charArray, char character) {
		int index = 0;
		for (char each : charArray) {
			if (each == character) {
				break;
			}
			index++;
		}
		return index;
	}
}
