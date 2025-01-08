
# -------------------
# Configures u-boot
# -------------------

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

UNPACKDIR = "${WORKDIR}/sources-unpack"

SRC_URI += "file://no_boot_delay.cfg"
