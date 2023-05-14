package com.adamve.hwkeyinfo.ui.service

import com.adamve.hwkeyinfo.data.Service

val serviceComparator = Comparator<Service> { l, r ->
    (l.serviceName + l.serviceUser).uppercase()
        .compareTo((r.serviceName + r.serviceUser).uppercase())
}