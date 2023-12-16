package nl.ricoapon.year2022.day7;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

public class FileSystem {
    public abstract static class Node {
        abstract String getName();
        abstract int getSize();
    }

    public static class Directory extends Node {
        private final String name;
        private final List<Node> children = new ArrayList<>();

        public Directory(String name) {
            this.name = name;
        }

        @Override
        String getName() {
            return name;
        }

        @Override
        int getSize() {
            return children.stream().mapToInt(n -> n.getSize()).sum();
        }

        public void addChild(Node node) {
            children.add(node);
        }
        
        public Optional<Node> getChildWithName(String name) {
            for (Node node : children) {
                if (node.getName().equals(name)) {
                    return Optional.of(node);
                }
            }
            return Optional.empty();
        }

        public List<Directory> getSubDirectories() {
            return children.stream().filter(n -> n instanceof Directory).map(n -> (Directory) n).toList();
        }
    }

    public static class File extends Node {
        private final String name;
        private final int size;

        public File(String name, int size) {
            this.name = name;
            this.size = size;
        }

        @Override
        String getName() {
            return name;
        }

        @Override
        int getSize() {
            return size;
        }
    }

    private final Directory root;
    private Stack<Directory> currentLocation;

    public FileSystem() {
        root = new Directory("/");
        currentLocation = new Stack<>();
        currentLocation.add(root);
    }

    public void moveIntoDirectory(String directory) {
        if ("/".equals(directory)) {
            currentLocation = new Stack<>();
            currentLocation.add(root);
        } else if ("..".equals(directory)) {
            // We have to make sure we cannot go outside of root.
            if (currentLocation.size() > 1) {
                currentLocation.pop();
            } else {
                throw new RuntimeException("This should not happen");
            }
        } else {
            Directory currentDir = currentLocation.peek();
            // We assume the directory was already created.
            Node newDir = currentDir.getChildWithName(directory).orElseThrow();
            if (!(newDir instanceof Directory)) {
                throw new RuntimeException("This should not happen");
            }

            currentLocation.add((Directory) newDir);
        }
    }

    public void createDirectory(String directoryName) {
        currentLocation.peek().addChild(new Directory(directoryName));
    }

    public void createFile(String fileName, int size) {
        currentLocation.peek().addChild(new File(fileName, size));
    }

    public List<Directory> getDirectoriesWithSizeAtMost100_000() {
        List<Directory> result = new ArrayList<>();

        Stack<Directory> subDirectoriesToCheck = new Stack<>();
        subDirectoriesToCheck.add(root);
        while (!subDirectoriesToCheck.isEmpty()) {
            Directory directory = subDirectoriesToCheck.pop();
            if (directory.getSize() <= 100_000) {
                result.add(directory);
            }

            subDirectoriesToCheck.addAll(directory.getSubDirectories());
        }

        return result;
    }

    public List<Directory> findDirectoriesWithSizeAtLeast(int size) {
        List<Directory> result = new ArrayList<>();

        Stack<Directory> subDirectoriesToCheck = new Stack<>();
        subDirectoriesToCheck.add(root);
        while (!subDirectoriesToCheck.isEmpty()) {
            Directory directory = subDirectoriesToCheck.pop();
            if (directory.getSize() >= size) {
                result.add(directory);
            }

            subDirectoriesToCheck.addAll(directory.getSubDirectories());
        }

        return result;
    }

    public int getTotalSize() {
        return root.getSize();
    }
}
