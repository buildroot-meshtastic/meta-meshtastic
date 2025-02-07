SUMMARY = "PlatformIO: The cross-platform build system for embedded development"
HOMEPAGE = "https://platformio.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=da91308434891f8f5b486c6ab9317309"

PYPI_PACKAGE = "platformio"
SRC_URI[sha256sum] = "79387b45ca7df9c0c51cae82b3b0a40ba78d11d87cea385db47e1033d781e959"

inherit setuptools3
inherit pypi
inherit python3native

DEPENDS += "\
    openssl-native \
    python3-click-native \
    python3-semantic-version-native \
    python3-requests-native \
    python3-tabulate-native \
    python3-pyserial-native \
    python3-pyelftools-native \
"

# Build this recipe for the host (native) rather than the target.
BBCLASSEXTEND = "native"
