package com.icms.user_auth.cli;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;

@Component 
@Profile("task")
public class SeedManager implements CommandLineRunner {
    
    private final List<DataSeed> seeds;

    public SeedManager(List<DataSeed> seeds) {
        this.seeds = seeds;
    }

    @Override
    public void run(String... args) throws Exception {
        String seedArg = Arrays.stream(args)
                .filter(arg -> arg.startsWith("--seed="))
                .map(arg -> arg.replace("--seed=", ""))
                .findFirst()
                .orElse(null);

        if (seedArg == null) {
            return;
        }

        if ("all".equalsIgnoreCase(seedArg)) {
            System.out.println("Ejecutando todos los seeds ordenados por prioridad...");
            seeds.stream()
                    .sorted((s1, s2) -> Integer.compare(s1.getOrder(), s2.getOrder()))
                    .forEach(seed -> seed.run());
        } else {
            seeds.stream()
                    .filter(s -> s.getName().equalsIgnoreCase(seedArg))
                    .findFirst()
                    .ifPresentOrElse(
                        seed -> seed.run(),
                        () -> {
                            throw new IllegalArgumentException("Seed not found: " + seedArg);
                        }
                    );
        }
    }
}
