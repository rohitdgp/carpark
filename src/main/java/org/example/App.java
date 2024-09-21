package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

/*
*
create lot 5
    parking lot created
park KA01AB1234 White
    Parked at slot 1
    * {
  "registrationNumber": "KA01AB1234",
  "color": "White"
}
park KA03DE3434 Blue
    Parked at slot 2
    * {
  "registrationNumber": "KA03DE3434",
  "color": "Blue"
}
park KA04BC6734 White
    Parked at slot 3
    * {
  "registrationNumber": "KA04BC6734",
  "color": "White"
}
leave Slot 2
    Leave KA03DE3434
park KA05VB4567 Yellow
    Parked at slot 2
status
     Slot 1 -> KA01AB1234 Car parked
     Slot 2 -> KA05VB4567 Car parked
     Slot 3 -> KA04BC6734 Car parked
     Slot 4 -> Empty
     Slot 5 -> Empty
color White
    Car with White Color : KA01AB1234, KA04BC6734
registration KA04BC6734
    registration KA04BC6734 parked at slot 3
* */