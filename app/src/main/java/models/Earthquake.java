package models;

import java.util.List;

/**
 * Created by Manuela.Stojceva on 3/17/2017.
 */
public class Earthquake {
    public String type;
    public Metadata metadata;
    public double[] bbox;
    public List<Feature> features;

    public class Metadata{
        public long generated;
        public String url, title, api;
        public int status, count;
    }

    public class Feature {
        public String type, id;
        public Properties properties;

        public class Properties{
            public double getMag() {
                return mag;
            }

            public void setPlace(String place) {
                this.place = place;
            }

            public void setMag(Double mag) {
                this.mag = mag;
            }
            public int tz, felt, sig, tsunami, nst;

            public String getPlace() {
                return place;
            }

            public String place, url, detail, alert, status, net, code, ids, sources, types, magType, type, title;
            public long time, updated;
            public double dmin, rms, gap, mag, cdi, mmi;

        }
        public class Geometry{
            public String type;
            public double[] coordinates;
        }

    }
}
