package com.addressbook;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.*;
import java.net.URI;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.google.gson.*;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AddressBook
{
    static ArrayList<Contact> list = new ArrayList<Contact>();
    public static AddressBook addressBook = new AddressBook(null);
    public static Contact contact=new Contact();

    static Scanner sc = new Scanner(System.in);
    public static ArrayList<AddressBook> book = new ArrayList<>();
    public HashMap<String,String> citydict=new HashMap<>();
    public HashMap<String,String> statedict=new HashMap<>();

    public Path path = Paths.get("C:\\Users\\Surendra\\IdeaProjects\\AddressBookSystem-UC13-onwards\\src\\main\\resources\\AddressBook.txt");
    public static final String csv_path = "C:\\Users\\Surendra\\IdeaProjects\\AddressBookSystem-UC13-onwards\\src\\main\\resources\\AddressBook.csv";
    public static final String json_path = "C:\\Users\\Surendra\\IdeaProjects\\AddressBookSystem-UC13-onwards\\src\\main\\resources\\AddressBook.json";

    public AddressBook(String str) {

    }

    public static void defaultBook() {
        book.add(new AddressBook("default address book"));
        book.add(new AddressBook("Address Book 1"));
        book.add(new AddressBook("Address Book 2"));
    }

    public void defaultContact(){
        book.get(0).list.add(new Contact("kaushal", "phutane", "parel", "mumbai", "maharashtra", "400025", "90290642", "kaushal@gmail.com"));
        book.get(0).list.add(new Contact("sumit", "wagh", "tilaknagar", "mumbai", "maharashtra", "400089", "816979161", "sumit@gmail.com"));
        book.get(1).list.add(new Contact("surendra", "chouhan", "wadala", "jodhpur", "rajastan", "4000012", "8181818818", "surendra@gmail.com"));
        book.get(1).list.add(new Contact("nikhil", "tiwari", "wadala", "thane", "bihar", "4000012", "1121221", "nikhil@gmail.com"));
        book.get(2).list.add(new Contact("gaurav", "purao", "kohinoor", "thane", "tamilnadu", "4040091", "82828882", "gaurav@gmail.com"));
    }

    public static void addAddressBook() {
        System.out.print("Enter name of new Address Book: ");
        String str=sc.next();
        book.add(new AddressBook(str));
    }

    public void searchPersonByCity() {
        System.out.println("Enter city for the contact info: ");
        String city=sc.next();

        System.out.println("\nContact details of Person in " + city + " city are : \n");
        list.stream().filter(contact -> contact.getCity().equals(city)).forEach(System.out::println);
    }

    public void searchPersonByState() {
        System.out.println("Enter state for the contact info: ");
        String state=sc.next();

        System.out.println("\nContact details of Person in " + state + " state are : \n");
        list.stream().filter(contact -> contact.getState().equals(state)).forEach(System.out::println);
    }

    public void personCityDictionary() {
        System.out.println("Enter City Name");
        String city=sc.next();

        book.forEach(address -> address.list.stream()
                .filter(contact -> contact.getCity().equals(city))
                .forEach(contact -> citydict.put((contact.getFirstName()), contact.getCity())));

        citydict.forEach((key, value) -> System.out.println(key));

        System.out.println("Count is : " + citydict.size());
    }

    public void personStateDictionary() {
        System.out.println("Enter State Name");
        String state=sc.next();

        book.forEach(address -> address.list.stream()
                .filter(contact -> contact.getState().equals(state))
                .forEach(contact -> statedict.put((contact.getFirstName()), contact.getCity())));

        statedict.forEach((key, value) -> System.out.println(key));

        System.out.println("Count is : " + statedict.size());
    }

    public void addDetails() throws IOException{
        System.out.println("How many contacts do you want to enter? ");
        int num=sc.nextInt();
        list.add(0,new Contact("omkar", "mali", "palaspe", "panvel", "maharastra", "4000129", "90290642", "omkar@gmail.com"));

        for(int i=0; i<num; i++) {
            System.out.println("Enter First Name");
            String firstName=sc.next();
            System.out.println("Enter Last Name");
            String lastName=sc.next();
            System.out.println("Enter Address");
            String address=sc.next();
            System.out.println("Enter City Name");
            String city=sc.next();
            System.out.println("Enter State Name");
            String state=sc.next();
            System.out.println("Enter Zip Code");
            String zip=sc.next();
            System.out.println("Enter Phone Number");
            String phoneNumber=sc.next();
            System.out.println("Enter Email");
            String email=sc.next();

            if(!firstName.equals(list.get(0).getFirstName())) {
                list.add( new Contact(firstName, lastName, address, city, state, zip, phoneNumber, email));
                System.out.println(list);
            }
            else {
                System.out.println("You have already entered this contact");
                break;
            }
        }

        Comparator<Contact> list1 = Comparator.comparing(Contact::getFirstName);
        System.out.println("\nAfter Sorting the contact details are : ");
        list.stream().sorted(list1).forEach(System.out::println);
    }

    public void writeDatatoFile() throws IOException {
        StringBuffer buffer = new StringBuffer();

        for(int i=0; i<list.size(); i++)
            Files.write(path, list.toString().getBytes());
    }

    public void readDatafromFile() throws IOException {
        Files.lines(path).forEach(System.out::println);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void writeDatatoCSV() throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        try{
            FileWriter writer = new FileWriter(csv_path);
            ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
            mappingStrategy.setType(Contact.class);

            String[] columns = new String[]{"FirstName", "LastName", "Address", "City", "State", "ZipCode", "PhoneNumber", "Email"};
            mappingStrategy.setColumnMapping(columns);

            StatefulBeanToCsvBuilder<Contact> builder = new StatefulBeanToCsvBuilder(writer);
            StatefulBeanToCsv beanWriter = builder.withMappingStrategy(mappingStrategy).build();
            beanWriter.write(list);
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readDataFromCSVfile() throws IOException {
        try {
            CSVReader reader = new CSVReader(new FileReader(csv_path));
            List<String[]> r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void writetoJsonFile() throws IOException {
        JSONObject obj = new JSONObject();
        try{
            for (Contact c : list){
                obj.put("First Name : ", c.getFirstName());
                obj.put("Last Name : ", c.getLastName());
                obj.put("Address : ", c.getAddress());
                obj.put("City : ", c.getCity());
                obj.put("State : ", c.getState());
                obj.put("Zip Code : ", c.getZip());
                obj.put("Phone Number : ", c.getPhoneNumber());
                obj.put("Email : ", c.getEmail());
                FileWriter writer = new FileWriter(json_path);
                writer.write(obj.toString());
                writer.flush();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }

    public void readFromJsonFile() throws IOException {
        FileReader reader = new FileReader(json_path);
        JSONParser parser = new JSONParser();
        try{
            System.out.println(parser.parse(reader));
        }
        catch (IOException | ParseException e){
            e.printStackTrace();
        }
    }
    
    public void sortByCity() {
        Comparator<Contact> list1 = Comparator.comparing(Contact::getCity);
        System.out.println("\n After Sorting the contact details by city : \n");
        list.stream().sorted(list1).forEach(System.out::println);
    }

    public void sortByState() {
        Comparator<Contact> list1 = Comparator.comparing(Contact::getState);
        System.out.println("\n After Sorting the contact details by State : \n");
        list.stream().sorted(list1).forEach(System.out::println);
    }

    private void displayDetails() {
        Comparator<Contact> list1 = Comparator.comparing(Contact::getFirstName);
        System.out.println("\n After Sorting the contact details are: \n");
        list.stream().sorted(list1).forEach(System.out::println);
    }

    public static String editDetails() {
        String name;
        System.out.println("Enter First Name of Details to be Edited: ");
        name = sc.next();

        if (name.equals(list.get(0).getFirstName())) {
            System.out.println("Enter FirstName");
            list.get(0).setFirstName(sc.next());
            System.out.println("Enter LastName");
            list.get(0).setLastName(sc.next());
            System.out.println("Enter Address");
            list.get(0).setAddress(sc.next());
            System.out.println("Enter CityName");
            list.get(0).setCity(sc.next());
            System.out.println("Enter StateName");
            list.get(0).setState(sc.next());
            System.out.println("Enter ZipCode");
            list.get(0).setZip(sc.next());
            System.out.println("Enter PhoneNumber");
            list.get(0).setPhoneNumber(sc.next());
            System.out.println("Enter Email");
            list.get(0).setEmail(sc.next());

            System.out.println(list.get(0));
            return "Contact Edited";
        }
        else {
            return "Name Not Available in List";
        }
    }

    public static String deleteContacts() {
        String name;
        System.out.print("Enter FirstName");
        name =sc.next();

        if (name.equals(list.get(0).getFirstName())) {
            list.remove(0);
            return "Deleted";
        }
        else {
            return "Name Not Available in List";
        }
    }

    public void choiceForReadWrite(){
        AddressBook address = new AddressBook(null);
        address.defaultBook();
        System.out.println("1. To write into .txt file \n" +
                           "2. To read from .txt file \n" +
                           "3. To write into .csv file \n" +
                           "4. To read from .csv file \n" +
                           "5. To write to .json file \n" +
                           "6. To read from .json file");
        int choice = sc.nextInt();
        try{
            switch (choice) {
                case 1:
                    address.writeDatatoFile();
                    address.readDatafromFile();
                    break;
                case 2:
                    address.readDatafromFile();
                    break;
                case 3:
                    address.writeDatatoCSV();
                    address.readDataFromCSVfile();
                    break;
                case 4:
                    address.readDataFromCSVfile();
                    break;
                case 5:
                    address.writetoJsonFile();
                    break;
                case 6:
                    address.readFromJsonFile();
                    break;
                default:
                    System.out.println("Invalid Input. Please enter again");
                    address.choiceForReadWrite();
                    break;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        System.out.println("Welcome To Address Book Problem\n");

        AddressBook address = new AddressBook(null);
        address.defaultBook();
        address.defaultContact();
        int check = 0;

        while(check != 12) {
            System.out.print("\n1.Add AddressBook \n2.Add Contact \n3.Display Contact \n4.Delete \n5.Edit"
                    + "\n6.Search for contacts based on city \n7.Search for contacts based on state"
                    + "\n8.To see name of a person based on city \n9.To see name of a person based on state \n10. Sort_By_City \n11.Sort_By_State \n12.Exit\n");
            check=sc.nextInt();

            switch(check) {
                case 1:
                    addAddressBook();
                    break;
                case 2:
                    try {
                        address.addDetails();
                        address.choiceForReadWrite();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    address.choiceForReadWrite();
                    break;
                case  4:
                    deleteContacts();
                    for(int i=0; i<list.size(); i++){
                        System.out.println(list.get(i));
                    }
                    break;
                case 5:
                    editDetails();
                    for(int i=0; i<list.size(); i++){
                        System.out.println(list.get(i));
                    }
                    break;
                case 6:
                    address.searchPersonByCity();
                    break;
                case 7:
                    address.searchPersonByState();
                    break;
                case 8:
                    address.personCityDictionary();
                    break;
                case 9:
                    address.personStateDictionary();
                    break;
                case 10:
                    address.sortByCity();
                    break;
                case 11:
                    address.sortByState();
                    break;
                case 12:
                    System.out.println("Thank you!!!");
                    break;
                default:
                    System.out.println("Wrong input");
            }
        }
    }
}
