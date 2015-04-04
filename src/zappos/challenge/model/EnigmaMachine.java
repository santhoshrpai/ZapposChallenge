package zappos.challenge.model;

public class EnigmaMachine {

	Rotor rotor1;
	Rotor rotor2;
	Rotor rotor3;
	Reflector reflector;

	public EnigmaMachine(Rotor rotor1, Rotor rotor2, Rotor rotor3,
			Reflector reflector) {
		this.rotor1 = rotor1;
		this.rotor2 = rotor2;
		this.rotor3 = rotor3;
		this.reflector = reflector;
	}

	private char decodeChar(char character) {

		incrementRotors();
		char encodedCharacter = rotor1.decodeRL(rotor2.decodeRL(rotor3
				.decodeRL(reflector.encodeLR(rotor3.decodeLR(rotor2
						.decodeLR(rotor1.decodeLR(character)))))));
		return encodedCharacter;
	}

	public String decodeLine(String line) {
		StringBuilder encodedLineBuilder = new StringBuilder();
		for (char character : line.toCharArray()) {
			char decodedChar = decodeChar(character);
			encodedLineBuilder.append(decodedChar);
		}
		return encodedLineBuilder.toString();
	}

	public void setRotors(char rotor1Position, char rotor2Position,
			char rotor3Position, char rotor1Notch, char rotor2Notch) {
		rotor1.set(rotor1Position);
		rotor2.set(rotor2Position);
		rotor3.set(rotor3Position);
		rotor1.setNotch(rotor1Notch);
		rotor2.setNotch(rotor2Notch);
		// step intially to start position
		rotor1.rotate(false);
		rotor2.rotate(false);
		rotor3.rotate(false);
	}

	private void incrementRotors() {
		boolean stepByOne = true;
		char rotor1Pos = rotor1.getStartPosition();
		char rotor2Pos = rotor2.getStartPosition();
		// check notch values
		rotor1.rotate(stepByOne);
		if (rotor1Pos == rotor1.getNotch()) {
			rotor2.rotate(stepByOne);
		}
		if (rotor2Pos == rotor2.getNotch()) {
			rotor3.rotate(stepByOne);
		}
	}
}
