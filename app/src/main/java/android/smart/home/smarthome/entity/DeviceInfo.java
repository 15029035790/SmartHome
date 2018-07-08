package android.smart.home.smarthome.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 *
 */

public class DeviceInfo {

    /**
     * errno : 0
     * data : {"private":true,"protocol":"HTTP","create_time":"2017-10-26 19:33:18","online":false,"location":{"lat":0,"lon":0},"id":"20080668","auth_info":"DS18B20","datastreams":[{"create_time":"2017-10-26 22:27:37","uuid":"f29babd2-6fbe-4bc0-b1e9-1a02e1e2229e","id":"CPU_temperature"},{"create_time":"2017-10-26 22:52:54","uuid":"7231d2f4-87d8-4829-a286-513ce926669c","id":"CPU_temperature1"},{"unit":"摄氏度","id":"temp_c","unit_symbol":"℃","create_time":null},{"unit":"华氏度","id":"temp_f","unit_symbol":"℉","create_time":null},{"unit":"摄氏度","id":"cpu_temperature","unit_symbol":"℃","create_time":null},{"unit":"摄氏度","id":"cpu_temperture1","unit_symbol":"℃","create_time":null}],"title":"DS18B20","tags":[]}
     * error : succ
     */

    private int errno;
    private DataBean data;
    private String error;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public static class DataBean {
        /**
         * private : true
         * protocol : HTTP
         * create_time : 2017-10-26 19:33:18
         * online : false
         * location : {"lat":0,"lon":0}
         * id : 20080668
         * auth_info : DS18B20
         * datastreams : [{"create_time":"2017-10-26 22:27:37","uuid":"f29babd2-6fbe-4bc0-b1e9-1a02e1e2229e","id":"CPU_temperature"},{"create_time":"2017-10-26 22:52:54","uuid":"7231d2f4-87d8-4829-a286-513ce926669c","id":"CPU_temperature1"},{"unit":"摄氏度","id":"temp_c","unit_symbol":"℃","create_time":null},{"unit":"华氏度","id":"temp_f","unit_symbol":"℉","create_time":null},{"unit":"摄氏度","id":"cpu_temperature","unit_symbol":"℃","create_time":null},{"unit":"摄氏度","id":"cpu_temperture1","unit_symbol":"℃","create_time":null}]
         * title : DS18B20
         * tags : []
         */

        @SerializedName("private")
        private boolean privateX;
        private String protocol;
        private String create_time;
        private boolean online;
        private LocationBean location;
        private String id;
        private String auth_info;
        private String title;
        private List<DatastreamsBean> datastreams;
        private List<?> tags;

        public boolean isPrivateX() {
            return privateX;
        }

        public void setPrivateX(boolean privateX) {
            this.privateX = privateX;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuth_info() {
            return auth_info;
        }

        public void setAuth_info(String auth_info) {
            this.auth_info = auth_info;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<DatastreamsBean> getDatastreams() {
            return datastreams;
        }

        public void setDatastreams(List<DatastreamsBean> datastreams) {
            this.datastreams = datastreams;
        }

        public List<?> getTags() {
            return tags;
        }

        public void setTags(List<?> tags) {
            this.tags = tags;
        }

        public static class LocationBean {
            /**
             * lat : 0
             * lon : 0
             */

            private int lat;
            private int lon;

            public int getLat() {
                return lat;
            }

            public void setLat(int lat) {
                this.lat = lat;
            }

            public int getLon() {
                return lon;
            }

            public void setLon(int lon) {
                this.lon = lon;
            }
        }

        public static class DatastreamsBean {
            /**
             * create_time : 2017-10-26 22:27:37
             * uuid : f29babd2-6fbe-4bc0-b1e9-1a02e1e2229e
             * id : CPU_temperature
             * unit : 摄氏度
             * unit_symbol : ℃
             */

            private String create_time;
            private String uuid;
            private String id;
            private String unit;
            private String unit_symbol;

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            public String getUnit_symbol() {
                return unit_symbol;
            }

            public void setUnit_symbol(String unit_symbol) {
                this.unit_symbol = unit_symbol;
            }
        }
    }
}
