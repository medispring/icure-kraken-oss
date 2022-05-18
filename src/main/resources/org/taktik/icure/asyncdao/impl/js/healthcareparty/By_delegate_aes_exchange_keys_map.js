map = function(doc) {
	if (doc.java_type === 'org.taktik.icure.entities.HealthcareParty' && !doc.deleted && (doc.hcPartyKeys || doc.aesExchangeKeys)) {
		if (doc.aesExchangeKeys) {
			var aesPubKeys = Object.keys(doc.aesExchangeKeys);
			Object.values(doc.aesExchangeKeys).forEach(function (ks) {
				Object.keys(ks).forEach(function (k) {
					var delegateKeys = ks[k];
					Object.entries(delegateKeys).forEach(function ([delPub, delK]) {
						if (!aesPubKeys[delPub.slice(-12)]) {
							emit(k, [doc._id, delPub.slice(-12), delK]);
						}
					})
				});
			});
		}

		if (!doc.aesExchangeKeys || !Object.keys(doc.aesExchangeKeys).includes(doc.publicKey)) {
			Object.keys(doc.hcPartyKeys).forEach(function (k) {
				emit(k, [doc._id, doc.publicKey.slice(-12), doc.hcPartyKeys[k][1]]);
			});
		}
	}
}
