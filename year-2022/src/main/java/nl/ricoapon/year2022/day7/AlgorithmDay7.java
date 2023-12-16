package nl.ricoapon.year2022.day7;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay7 implements Algorithm {
    private FileSystem parseCommandsIntoFileSystem(String input) {
        FileSystem fileSystem = new FileSystem();

        for (String command : input.split("\\$")) {
            command = command.trim();
            if (command.length() == 0) {
                continue;
            }

            if (command.startsWith("cd")) {
                fileSystem.moveIntoDirectory(command.substring(3));
            } else if (command.startsWith("ls")) {
                List<String> nodes = Arrays.stream(command.split("\r?\n")).skip(1).toList();
                for (String node : nodes) {
                    if (node.startsWith("dir")) {
                        fileSystem.createDirectory(node.substring(4));
                    } else {
                        int size = Integer.parseInt(node.split(" ")[0]);
                        String filename = node.split(" ")[1];
                        fileSystem.createFile(filename, size);
                    }
                }
            } else {
                throw new RuntimeException("This should not happen");
            }
        }

        return fileSystem;
    }

    @Override
    public String part1(String input) {
        FileSystem fileSystem = parseCommandsIntoFileSystem(input);
        List<FileSystem.Directory> smallDirectories = fileSystem.getDirectoriesWithSizeAtMost100_000();
        return "" + smallDirectories.stream().mapToInt(FileSystem.Directory::getSize).sum();
    }

    @Override
    public String part2(String input) {
        FileSystem fileSystem = parseCommandsIntoFileSystem(input);
        int sizeUsed = fileSystem.getTotalSize();
        int maxSize = 70_000_000;
        int requiredFreeSpace = 30_000_000;
        int currentFreeSpace = maxSize - sizeUsed;
        if (requiredFreeSpace < currentFreeSpace) {
            throw new RuntimeException("This should never happen");
        }

        int needToFreeUp = requiredFreeSpace - currentFreeSpace;
        List<FileSystem.Directory> possibleDirectories = fileSystem.findDirectoriesWithSizeAtLeast(needToFreeUp);

        FileSystem.Directory smallest = possibleDirectories.stream()
            .sorted(Comparator.comparingInt(FileSystem.Directory::getSize))
            .findFirst().orElseThrow();

        return "" + smallest.getSize();
    }
}
