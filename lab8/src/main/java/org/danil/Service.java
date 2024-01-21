package org.danil;

import java.util.Date;
import java.util.List;

interface Service {
    @Cache()
    List<String> run(String item, double value, Date date);

    @Cache()
    List<String> work(String item);
}
