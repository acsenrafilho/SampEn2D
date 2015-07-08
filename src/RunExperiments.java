/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import classes.ImageAccess;
import classes.SampEn2D;
import ij.ImagePlus;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JTextArea;

/**
 *
 * @author antonio
 */
public class RunExperiments {

    private int[] m;
    private double[] r;
    private String inputFolderPath;
    private String outputFolderPath;
    private JTextArea log;

    public RunExperiments(String inputFolder, String outputFolder, int[] m, double[] r, JTextArea log) {
        this.m = m;
        this.r = r;
        this.inputFolderPath = inputFolder;
        this.outputFolderPath = outputFolder;
        this.log=log;
    }

    public void SampEn2DExperiment() throws IOException {
        SampEn2D se = new SampEn2D();
        File outputFolder = new File(outputFolderPath);
        outputFolder.mkdir();
        BufferedWriter bwB;
        ImageAccess imageAux;

        File[] groupsFolders = new File(inputFolderPath).listFiles();
//        Arrays.sort(imgsFolders);

        for (File f : groupsFolders) {
            log.append("Calculating on group: "+f.getName()+"\n");
            File[] imgs = f.listFiles();
            for (int mValue : m) {
                bwB = new BufferedWriter(new FileWriter(outputFolderPath + "/" + f.getName() + "_sampEn2D_m" + mValue + ".csv"));

                bwB.write("image");
                for (double rValue : r) {
                    bwB.write("\tr" + rValue);
                }
                bwB.newLine();

                for (File imgSelected : imgs) {
                    log.append("m = "+mValue+", Image: "+imgSelected.getName()+"\n");
                    bwB.write(imgSelected.getName());
                    imageAux = new ImageAccess(new ImagePlus(imgSelected.getAbsolutePath()).getProcessor().convertToByte(true));
                    for (double rValue : r ) {
                        bwB.write("\t" + se.fastSampleEn2D(imageAux, mValue, rValue));
                    }                    
                    bwB.newLine();
                }
                bwB.close();
                log.append("---*---*---*---*---*---\n\n");
                        
            }
        }
    }

}
