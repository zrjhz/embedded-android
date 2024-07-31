package car.car2024.Utils.Algorithm;

public class LFSR {
    private final int[] taps;
    private final int[] state;

    public LFSR(int[] taps, int[] initialState) {
        this.taps = taps;
        this.state = initialState;
    }

    public void clock() {
        int feedbackBit = 0;
        for (int tap : taps) {
            feedbackBit ^= state[tap];
        }

        // Shift the state to the right
        System.arraycopy(state, 0, state, 1, state.length - 1);

        // Set the most significant bit to the feedback bit
        state[0] = feedbackBit;
    }

    public byte[] encrypt(byte[] data) {
        byte[] encryptedData = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < 8; j++) {
                clock();
                encryptedData[i] |= (byte) (state[0] << (7 - j));
            }
        }
        return encryptedData;
    }

    public byte[] decrypt(byte[] encryptedData) {
        return encrypt(encryptedData); // LFSR decryption is the same as encryption
    }
}