package collection;

import builders.HumanDirector;
import exceptions.ValidException;
import humans.HumanBeing;
import humans.Mood;
import humans.WeaponType;
import io.XMLFileWriter;
import utils.FileChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HumanSet {
    private final Set<HumanBeing> collection;
    private final HumanDirector humanDirector;
    private final LocalDateTime creationDate;
    private final Validator<HumanBeing> validator;
    private final XMLFileWriter<HumanBeing> writer;
    private final File file;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    public HumanSet(Collection<HumanBeing> collection, HumanDirector humanDirector, File file, Validator<HumanBeing> validator, XMLFileWriter<HumanBeing> writer) {
        this.humanDirector = humanDirector;
        this.writer = writer;
        this.collection = new LinkedHashSet<>();
        this.validator = validator;
        collection.forEach(this::add);
        this.file = file;
        this.creationDate = LocalDateTime.now();
        System.out.println("Collection was initialized");
    }

    public Set<HumanBeing> getCollection() {
        return collection;
    }

    public String add(HumanBeing element) {
        try {
            String message;
            if (validator.checkElement(element) && !checkElementById(element.getId())) {
                writeLock.lock();
                collection.add(element);
                writeLock.unlock();
                message = String.format("HumanBeing with id: %s was added to collection", element.getId().toString());
            } else {
                message = String.format("element: %s is already exists", element.getId().toString());
            }
            return message;
        } catch (ValidException e) {
            return e.getMessage();
        }
    }

    public String addIfMin(HumanBeing humanBeing) {
        if (collection.size() == 0) {
            return add(humanBeing);
        }
        if (getMinElement().get().compareTo(humanBeing) > 0) {
            return add(humanBeing);
        } else {
            return "Human more than min of collection or equals it";
        }
    }

    public boolean checkElementById(UUID id) {
        return collection.stream()
                .map(HumanBeing::getId)
                .anyMatch(uuid -> uuid.equals(id));
    }

    public Optional<HumanBeing> getMinElement() {
        return collection.stream().min(Comparator.comparingLong(HumanBeing::getImpactSpeed));
    }

    public void clear() {
        collection.clear();
    }

    public void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

    public HumanDirector getHumanDirector() {
        return humanDirector;
    }

    public boolean removeById(UUID id) {
        writeLock.lock();
        readLock.lock();
        boolean isRemoved = collection.removeIf(product -> product.getId().equals(id));
        writeLock.unlock();
        readLock.unlock();
        return isRemoved;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (MainCollectible human : collection) {
            str.append(human.toString()).append("\n");
        }
        return str.toString();
    }

    public void removeLower(HumanBeing humanBeing) {
        collection.removeIf(humanBeing1 -> humanBeing1.compareTo(humanBeing) < 0);
    }

    public int countLessWeapon(WeaponType weaponType) {
        int res = 0;
        for (HumanBeing humanBeing : collection) {
            if (humanBeing.getWeaponType().getNumber() - weaponType.getNumber() < 0) {
                res++;
            }
        }
        return res;
    }

    public String filterByImpactSpeed(String arg) {
        int speed = Integer.parseInt(arg);
        StringBuilder str = new StringBuilder();
        for (MainCollectible human : collection) {
            if (speed == human.getImpactSpeed()) {
                str.append(human).append("\n");
            }
        }
        return str.toString();
    }

    public String filterGreaterThanMood(Mood mood) {
        StringBuilder str = new StringBuilder();
        for (MainCollectible human : collection) {
            Mood collectionMood = human.getMood();
            if (collectionMood != null && collectionMood.getNumber() - mood.getNumber() > 0) {
                str.append(human).append("\n");
            }
        }
        return str.toString();
    }

    public void save(String[] arg) throws FileNotFoundException {
        if (arg.length == 0) {
            writer.writeCollectionToFile(file, collection);
        } else {
            File newFile = new File(arg[0]);
            if (FileChecker.checkFileToWrite(newFile)) {
                writer.writeCollectionToFile(newFile, collection);
            } else {
                throw new FileNotFoundException(String.format("With name - %s", arg[0]));
            }
        }
    }
    // info, show, removeById,
//    public static int getFreeUrinals(String urinals) {
//        int result = 0;
//
//        urinals = urinals.replaceAll("10", "").replaceAll("^0+", "").replaceAll("0+$", "").replaceAll("01");
//
//        String[] fragments = urinals.split("1");
//        for (String fragment : fragments) {
//            result += (fragment.length() + 1) / 2;
//        }
//
//        return result;
//    }
}
//package org.example;
//import org.testng.annotations.Test;
//
//import static org.testng.Assert.assertEquals;
//
//    public class Main {
//        public static void main(String[] args) {
//            System.out.println("Hello world!");
//            System.out.println("Fixed Tests: noBoringZeros");
//            testing(noBoringZeros(1450), 145);
//            testing(noBoringZeros(960000), 96);
//            testing(noBoringZeros(1050), 105);
//            testing(noBoringZeros(-1050), -105);
//
//        }
//        public static int noBoringZeros(int n) {
//            while (n % 10 == 0){
//                n = n/10;
//            } return n;
//        }
//        private static void testing(int actual, int expected) {
//            assertEquals(expected, actual);
//        }
//    }