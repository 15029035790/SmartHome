package android.smart.home.smarthome.entity;

import java.util.List;

/**
 * Created by TangZiWen on 2017/12/18.
 *
 */

public class OnenetDataPoints {

    /**
     * errno : 0
     * error : “succ”
     * data : {"cursor":"70921_3771908_1472355096","datastreams":[{"id":"temperature","datapoints":[{"at":"10:22:22","value":42},{"at":"10:22:22","value":84}]},{"id":"key","datapoints":[{"at":" 10:22:22","value":24},{"at":"10:22:22","value":32}]}]}
     */

    private int errno;
    private String error;
    private DataBean data;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * cursor : 70921_3771908_1472355096
         * datastreams : [{"id":"temperature","datapoints":[{"at":"10:22:22","value":42},{"at":"10:22:22","value":84}]},{"id":"key","datapoints":[{"at":" 10:22:22","value":24},{"at":"10:22:22","value":32}]}]
         */
        private int count;
        private String cursor;
        private List<DatastreamsBean> datastreams;

        public int getCount(){return count;}

        public void setCount(int count) {
            this.count = count;
        }

        public String getCursor() {
            return cursor;
        }

        public void setCursor(String cursor) {
            this.cursor = cursor;
        }

        public List<DatastreamsBean> getDatastreams() {
            return datastreams;
        }

        public void setDatastreams(List<DatastreamsBean> datastreams) {
            this.datastreams = datastreams;
        }

        public static class DatastreamsBean {
            /**
             * id : temperature
             * datapoints : [{"at":"10:22:22","value":42},{"at":"10:22:22","value":84}]
             */

            private String id;
            private List<DatapointsBean> datapoints;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public List<DatapointsBean> getDatapoints() {
                return datapoints;
            }

            public void setDatapoints(List<DatapointsBean> datapoints) {
                this.datapoints = datapoints;
            }

            public static class DatapointsBean {
                /**
                 * at : 10:22:22
                 * value : 42
                 */

                private String at;
                private Float value;

                public String getAt() {
                    return at;
                }

                public void setAt(String at) {
                    this.at = at;
                }

                public Float getValue() {
                    return value;
                }

                public void setValue(Float value) {
                    this.value = value;
                }
            }
        }
    }
}
