package com.adamve.hwkeyinfo.ui.service

import com.adamve.hwkeyinfo.data.Service
import com.adamve.hwkeyinfo.data.ServiceWithSecurityKeys

val serviceComparator = Comparator<Service> { l, r ->
    (l.serviceName + l.serviceUser).uppercase()
        .compareTo((r.serviceName + r.serviceUser).uppercase())
}

val serviceWithSecurityKeysComparator = Comparator<ServiceWithSecurityKeys> { l, r ->
    (l.service.serviceName + l.service.serviceUser).uppercase()
        .compareTo((r.service.serviceName + r.service.serviceUser).uppercase())
}