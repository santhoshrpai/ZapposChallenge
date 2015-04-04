package zappos.challenge.driver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import zappos.challenge.model.EnigmaMachine;
import zappos.challenge.model.Reflector;
import zappos.challenge.model.Rotor;

public class Decrypt {

	public static final String INPUT_FILE_PATH = "src/zappos/challenge/resource/encrypted.text";
	private static final String IMAGE_FILE_PATH = "src/zappos/challenge/resource/asu_challenge.bmp";

	public static void main(String[] args) throws IOException {

		ImageReader reader = new ImageReader();
		String msg = reader.readAndSave(IMAGE_FILE_PATH);

		String staticWheel = reader.getStaticWheel(msg);

		Rotor rotor1 = new Rotor(reader.getRotor1(msg), staticWheel);
		Rotor rotor2 = new Rotor(reader.getRotor2(msg), staticWheel);
		Rotor rotor3 = new Rotor(reader.getRotor3(msg), staticWheel);
		Reflector reflector = new Reflector(reader.getReflector(msg));

		char rotor1StartPosition = reader.getRotor1Start(msg);
		char rotor2StartPosition = reader.getRotor2Start(msg);
		char rotor3StartPosition = reader.getRotor3Start(msg);
		char rotor1Notch = reader.getRotor1Notch(msg);
		char rotor2Notch = reader.getRotor1Notch(msg);

		EnigmaMachine enigmaMachine = new EnigmaMachine(rotor1, rotor2, rotor3,
				reflector);

		enigmaMachine.setRotors(rotor1StartPosition, rotor2StartPosition,
				rotor3StartPosition, rotor1Notch, rotor2Notch);

		decrypt(INPUT_FILE_PATH, enigmaMachine);
	}

	private static void decrypt(String inputFilePath,
			EnigmaMachine enigmaMachine) throws IOException {
		BufferedReader reader = new BufferedReader(
				new FileReader(inputFilePath));
		String line = reader.readLine();
		while (line != null) {
			if (!line.isEmpty()) {
				System.out.println("Decoded message: "
						+ enigmaMachine.decodeLine(line));
			}
			line = reader.readLine();
		}
		reader.close();
	}
}
