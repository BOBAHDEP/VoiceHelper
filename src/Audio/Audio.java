package Audio;

import GoogleApi.GoogleSpeech;
import javaFlacEncoder.FLACFileWriter;

import javax.sound.sampled.*;
import java.io.*;


public class Audio {
    static final long RECORD_TIME = 6000;  // 6 sec
    private String result = "";

    // the line from which audio data is captured
    TargetDataLine line;

    AudioFormat getAudioFormat() {
        float sampleRate = 16000;
        int sampleSizeInBits = 8;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = false;
        return new AudioFormat(sampleRate, sampleSizeInBits,
                channels, signed, bigEndian);
    }

    void start() {      //Captures the sound and record
        try {
            AudioFormat format = getAudioFormat();
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            // checks if system supports the data line
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Line not supported");
                System.exit(0);
            }
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();   // start capturing

            System.out.println("Start capturing...");

            AudioInputStream ais = new AudioInputStream(line);

            System.out.println("Start recording...");

            ByteArrayOutputStream file = new ByteArrayOutputStream();
            AudioSystem.write(ais, FLACFileWriter.FLAC, file);
            byte[] bytes = file.toByteArray();
            result = GoogleSpeech.getString(bytes);

        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    void finish() {         //Closes the target data line to finish capturing and recording
        line.stop();
        line.close();
        System.out.println("Finished");
    }

    public String record() {
        final Audio recorder = new Audio();

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });

        stopper.start();

        // start recording
        recorder.start();
        return recorder.result;
    }

    public static void main(String[] args) {
        final Audio recorder = new Audio();

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(RECORD_TIME);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });

        stopper.start();

        // start recording
        recorder.start();
    }
}