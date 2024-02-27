package org.danil;

import javax.xml.transform.Source;

public @interface Cachable {
    Class<? extends Source> value();
}
