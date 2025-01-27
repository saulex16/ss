package ar.edu.itba.ss.input;

import ar.edu.itba.ss.Main;
import ar.edu.itba.ss.models.Particle;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class StaticFile {

    private  int l;

    private int n;

    private double rc;

    private double maxR = 0.0;

    private final List<Particle> particles = new ArrayList<>();

    public StaticFile(String filename) throws FileNotFoundException {

        if(filename == null)
            throw  new FileNotFoundException();

        Scanner scanner = new Scanner(new File(Paths.get("input/" +filename).toAbsolutePath().toString()));

        if (scanner.hasNextLine()) {
            n = Integer.parseInt(scanner.nextLine().trim());
        }

        if(scanner.hasNextLine()){
            l = Integer.parseInt(scanner.nextLine().trim());
        }

        if(scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2) {
                double r = Double.parseDouble(parts[0]);
                this.maxR = Math.max(this.maxR,r);
                //We assume all rc are the same
                rc = Double.parseDouble(parts[1]);
                particles.add(new Particle(r));
            }
        }

        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2) {
                double r = Double.parseDouble(parts[0]);
                particles.add(new Particle(r));
            }
        }
    }

    public int getL() {
        return l;
    }

    public int getN() {
        return n;
    }

    public double getRc() {
        return rc;
    }

    public double getMaxR() {
        return maxR;
    }

    public List<Particle> getParticles() {
        return particles;
    }
}
