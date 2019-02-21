package io.github.primepotato.jandas.demos;

import io.github.primepotato.jandas.dataframe.DataFrame;
import io.github.primepotato.jandas.utils.Jandas;

public class JandasDemo {
    public static void main(String[] args) {
        DataFrame df = Jandas.readCsv("src/main/resources/party_constituency_vote_shares.csv");
        df.createColumn("avg", "(Con+Lab)/2.0");
        df.print();
    }
}
