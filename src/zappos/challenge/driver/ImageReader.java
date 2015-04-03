package zappos.challenge.driver;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageReader {

	private static final int DATA_SIZE = 8;
	private static final int MAX_INT_LEN = 4;
	private static final int INITIAL_OFFSET = 54;
	private static final int ROTOR_BYTES_SIZE = 94;
	private static final int ROTOR_START_NOTCH_BYTE_SIZE = 1;

	public String getStaticWheel(String msg) {
		return msg.substring(0, ROTOR_BYTES_SIZE);
	}

	public String getReflector(String msg) {
		return msg.substring(ROTOR_BYTES_SIZE, 2 * ROTOR_BYTES_SIZE);
	}

	public String getRotor1(String msg) {
		return msg.substring(2 * ROTOR_BYTES_SIZE, 3 * ROTOR_BYTES_SIZE);
	}

	public String getRotor2(String msg) {
		return msg.substring(3 * ROTOR_BYTES_SIZE, 4 * ROTOR_BYTES_SIZE);
	}

	public String getRotor3(String msg) {
		return msg.substring(4 * ROTOR_BYTES_SIZE, 5 * ROTOR_BYTES_SIZE);
	}

	public char getRotor1Start(String msg) {
		return msg.substring(
				5 * ROTOR_BYTES_SIZE + 2 * ROTOR_START_NOTCH_BYTE_SIZE,
				5 * ROTOR_BYTES_SIZE + 3 * ROTOR_START_NOTCH_BYTE_SIZE).charAt(
				0);
	}

	public char getRotor2Start(String msg) {
		return msg.substring(
				5 * ROTOR_BYTES_SIZE + 3 * ROTOR_START_NOTCH_BYTE_SIZE,
				5 * ROTOR_BYTES_SIZE + 4 * ROTOR_START_NOTCH_BYTE_SIZE).charAt(
				0);
	}

	public char getRotor3Start(String msg) {
		return msg.substring(
				5 * ROTOR_BYTES_SIZE + 4 * ROTOR_START_NOTCH_BYTE_SIZE,
				5 * ROTOR_BYTES_SIZE + 5 * ROTOR_START_NOTCH_BYTE_SIZE).charAt(
				0);
	}

	public char getRotor1Notch(String msg) {
		return msg.substring(5 * ROTOR_BYTES_SIZE,
				5 * ROTOR_BYTES_SIZE + ROTOR_START_NOTCH_BYTE_SIZE).charAt(0);
	}

	public char getRotor2Notch(String msg) {
		return msg.substring(
				5 * ROTOR_BYTES_SIZE + ROTOR_START_NOTCH_BYTE_SIZE,
				5 * ROTOR_BYTES_SIZE + 2 * ROTOR_START_NOTCH_BYTE_SIZE).charAt(
				0);
	}

	public String readAndSave(String filePath) throws IOException {
		Path path = Paths.get(filePath);

		byte[] imBytes = Files.readAllBytes(path);

		int msgLen = getMsgLength(imBytes, INITIAL_OFFSET);

		String msg = getMessage(imBytes, msgLen, MAX_INT_LEN * DATA_SIZE
				+ INITIAL_OFFSET);

		String data = msg.substring(5 * ROTOR_BYTES_SIZE + 5
				* ROTOR_START_NOTCH_BYTE_SIZE, msgLen - 3);

		System.out.println("Encoded message: " + data);

		PrintWriter writer = new PrintWriter(Decrypt.INPUT_FILE_PATH);
		writer.println(data);
		writer.close();
		return msg;

	}

	private static int getMsgLength(byte[] imBytes, int offset) {
		byte[] lenBytes = extractHiddenBytes(imBytes, MAX_INT_LEN, offset);
		int msgLen = ((lenBytes[0] & 0xff) << 24)
				| ((lenBytes[1] & 0xff) << 16) | ((lenBytes[2] & 0xff) << 8)
				| (lenBytes[3] & 0xff);
		return msgLen;
	}

	private static byte[] extractHiddenBytes(byte[] imBytes, int size,
			int offset) {
		byte[] hiddenBytes = new byte[size];
		for (int j = 0; j < size; j++) {
			for (int i = 0; i < DATA_SIZE; i++) {
				hiddenBytes[j] = (byte) ((hiddenBytes[j] << 1) | (imBytes[offset] & 1));
				offset++;
			}
		}
		return hiddenBytes;
	}

	private static String getMessage(byte[] imBytes, int msgLen, int offset) {
		byte[] msgBytes = extractHiddenBytes(imBytes, msgLen, offset);
		if (msgBytes == null)
			return null;
		String msg = new String(msgBytes);
		return msg;
	}

}
