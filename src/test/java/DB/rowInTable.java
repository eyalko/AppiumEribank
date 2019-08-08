package DB;

public class rowInTable {
    public String cloud_version,dhm_os,device_udid,device_version,test_name,command_type,command_xpath,command_duration,device_os;

    public rowInTable(String cloud_version, String dhm_os, String device_udid, String device_version, String test_name, String command_type, String command_xpath, String command_duration, String device_os) {
        this.cloud_version = cloud_version;
        this.dhm_os = dhm_os;
        switch (dhm_os) {
            case "192.168.2.146":
                this.dhm_os="Windows";
                break;
            case "192.168.2.96":
            case "192.168.2.105":
            case "192.168.2.70":
                this.dhm_os="Mac";
                break;
            default:
                this.dhm_os="Unknown DHM";
        }
        this.device_udid = device_udid;
        this.device_version = device_version;
        this.test_name = test_name;
        this.command_type = command_type;
        this.command_xpath = command_xpath;
        this.command_duration = command_duration;
        this.device_os = device_os;
    }

    @Override
    public String toString() {
        return "rowInTable{" +
                " cloud_version='" + cloud_version + '\'' +
                ", dhm_os='" + dhm_os + '\'' +
                ", device_udid='" + device_udid + '\'' +
                ", device_version='" + device_version + '\'' +
                ", test_name='" + test_name + '\'' +
                ", command_type='" + command_type + '\'' +
                ", command_xpath='" + command_xpath + '\'' +
                ", command_duration='" + command_duration + '\'' +
                ", device_os='" + device_os + '\'' +
                '}';
    }
}
