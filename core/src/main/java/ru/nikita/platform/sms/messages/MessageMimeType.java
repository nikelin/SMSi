package ru.nikita.platform.sms.messages;

public class MessageMimeType {
    public enum MimeType {
        MIME_TYPE_SM_RING("sm/ring"),
        MIME_TYPE_OTA_RING("ota/ring"),
        MIME_TYPE_EMS_RING("ems/ring"),
        MIME_TYPE_SM_LOGO("sm/logo"),
        MIME_TYPE_SM_PIC("sm/pic"),
        MIME_TYPE_OTA_PIC("ota/pic"),
        MIME_TYPE_EMS_PIC("ems/pic"),
        MIME_TYPE_EMS_VPIC("ems/vpic"),
        MIME_TYPE_SMS("sms"),
        BINARY("binray"),
        DEFAULT("");

        private String type;

        private MimeType(String type) {
            this.type = type;
        }

        public String type() {
            return this.type;
        }

    }
}