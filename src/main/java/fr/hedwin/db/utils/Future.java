/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Projet 2A
 Author: Edwin HELET & Julien GUY
 Class: Future.java
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.utils;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public interface Future<V> extends Callable<V> {

    Future<V> then(Consumer<V> consumer);
    Future<V> error(Consumer<Exception> error);
    Future<V> thenFinally(Runnable runnable);

}
