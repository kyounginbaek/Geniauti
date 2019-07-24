import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';

admin.initializeApp(functions.config().firebase);


// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

const db = admin.firestore();

// exports.testCreate = functions.firestore
//     .document('test/{testID}').onCreate((snap, context) => {
//         console.log('On Create triggered...');
//         console.log(context.params.testID);
//         // var test = db.collection('test').doc('abc').set({abc:3});

//         // const newValue = change.data();
//         console.log(context.eventType);
//     });

// exports.testUpdate = functions.firestore
// .document('test/{testID}').onUpdate((change, context) => {
//     console.log('On Update triggered...');
//     console.log(context.params.testID);
//     // var test = db.collection('test').doc('abc').set({abc:3});

//     // const newValue = change.data();
//     console.log(context.eventType);
// });

// exports.testDelete = functions.firestore
// .document('test/{testID}').onDelete((snap, context) => {
//     console.log('On Delete triggered...');
//     console.log(context.params.testID);
//     // var test = db.collection('test').doc('abc').set({abc:3});

//     // const newValue = change.data();
//     console.log(context.eventType);
// });


exports.testFunction = functions.firestore
.document('behaviors/{bid}').onWrite((change, context) => {//.region('asia-northeast1')
    console.log('Hello World...');
    console.log(context.params.bid);

    const document = change.after.exists ? change.after.data() : null;
    const oldDocument = change.before.data();

    if(document === null) {
        console.log("Delete Method", oldDocument);

        const d = new Date(oldDocument.start_time.getTime() + 540 * 60 * 1000);

        const month = ("0" + (d.getMonth() + 1)).slice(-2);
        const date = ("0" + d.getDate()).slice(-2);

        const dayFormat = d.getFullYear().toString()+month+date;
        const monthFormat = d.getFullYear().toString()+month;
        const yearFormat = d.getFullYear().toString();
        const duration_min = (oldDocument.end_time.getTime() - oldDocument.start_time.getTime())/60000
    
        const dayRef = db.collection('statistics').doc(oldDocument.cid).collection('day')
            .doc(dayFormat);
        const monthRef = db.collection('statistics').doc(oldDocument.cid).collection('month')
            .doc(monthFormat);
        const yearRef = db.collection('statistics').doc(oldDocument.cid).collection('year')
            .doc(yearFormat);

        //Day Delete
        const dayDeletion = db.runTransaction(t => {
            return t.get(dayRef)
                .then(docs => {
                let data = docs.data(); //doc랑 docs랑 같음
                    data["summary"]["count"] -= 1;

                    if (data["summary"]["count"] <= 0) {
                        data = {};
                        t.delete(dayRef);
                    } else {
                        data["behavior_freq"][d.getHours()] -= 1;
                        if (data["behavior_freq"][d.getHours()] === 0) {
                            delete data["behavior_freq"][d.getHours()];
                        }
                        
                        data["summary"]["duration_min"] -= duration_min;
                        data["summary"]["intensity_sum"] -= oldDocument.intensity;
                        for (const i in oldDocument.type) {
                            data["type"][i] -= 1;
                            if (data["type"][i] === 0) {
                                //값이 0이면
                                delete data["type"][i];
                            }
                        }
                        for (const i in oldDocument.reason) {
                            data["reason_type"][i] -= 1;
                            if (data["reason_type"][i] === 0) {
                                //값이 0이면
                                delete data["reason_type"][i];
                            }
                        }
                        data["place"][oldDocument.place] -= 1;
                        if (data["place"][oldDocument.place] === 0) {
                            //값이 0이면
                            delete data["place"][oldDocument.place];
                        }
                        t.update(dayRef, data);
                    }
                });
        }).then(result => {
            console.log('Day Delection Transaction success!');
        }).catch(err => {
            console.log('Day Delection Transaction failure:', err);
        });

        const monthDelection = db.runTransaction(t => {
            return t.get(monthRef)
                .then(docs => {
                let data = docs.data(); //doc랑 docs랑 같음
                    data["summary"]["count"] -= 1;

                    if (data["summary"]["count"] <= 0) {
                        data = {};
                        t.delete(monthRef);
                    } else {
                        data["behavior_freq"][d.getDate()] -= 1;
                        if (data["behavior_freq"][d.getDate()] === 0) {
                            delete data["behavior_freq"][d.getDate()];
                        }
                        
                        data["summary"]["duration_min"] -= duration_min;
                        data["summary"]["intensity_sum"] -= oldDocument.intensity;
                        for (const i in oldDocument.type) {
                            data["type"][i] -= 1;
                            if (data["type"][i] === 0) {
                                //값이 0이면
                                delete data["type"][i];
                            }
                        }
                        for (const i in oldDocument.reason) {
                            data["reason_type"][i] -= 1;
                            if (data["reason_type"][i] === 0) {
                                //값이 0이면
                                delete data["reason_type"][i];
                            }
                        }
                        data["place"][oldDocument.place] -= 1;
                        if (data["place"][oldDocument.place] === 0) {
                            //값이 0이면
                            delete data["place"][oldDocument.place];
                        }
                        t.update(monthRef, data);
                    }
                });
        }).then(result => {
            console.log('Month Delection Transaction success!');
        }).catch(err => {
            console.log('Month Delection Transaction failure:', err);
        });

        const yearDelection = db.runTransaction(t => {
            return t.get(yearRef)
                .then(docs => {
                let data = docs.data(); //doc랑 docs랑 같음
                    data["summary"]["count"] -= 1;

                    if (data["summary"]["count"] <= 0) {
                        data = {};
                        t.delete(yearRef);
                    } else {
                        data["behavior_freq"][d.getMonth()+1] -= 1;
                        if (data["behavior_freq"][d.getMonth()+1] === 0) {
                            delete data["behavior_freq"][d.getMonth()+1];
                        }
                        
                        data["summary"]["duration_min"] -= duration_min;
                        data["summary"]["intensity_sum"] -= oldDocument.intensity;
                        for (const i in oldDocument.type) {
                            data["type"][i] -= 1;
                            if (data["type"][i] === 0) {
                                //값이 0이면
                                delete data["type"][i];
                            }
                        }
                        for (const i in oldDocument.reason) {
                            data["reason_type"][i] -= 1;
                            if (data["reason_type"][i] === 0) {
                                //값이 0이면
                                delete data["reason_type"][i];
                            }
                        }
                        data["place"][oldDocument.place] -= 1;
                        if (data["place"][oldDocument.place] === 0) {
                            //값이 0이면
                            delete data["place"][oldDocument.place];
                        }
                        t.update(yearRef, data);
                    }
                });
        }).then(result => {
            console.log('Year Delection Transaction success!');
        }).catch(err => {
            console.log('Year Delection Transaction failure:', err);
        });

        return null

    }

    else if(oldDocument == null) {
        console.log("Create Method");
        const d = new Date(document.start_time.getTime() + 540 * 60 * 1000);

        const month = ("0" + (d.getMonth() + 1)).slice(-2);
        const date = ("0" + d.getDate()).slice(-2);
        
        const dayFormat = d.getFullYear().toString()+month+date;
        const monthFormat = d.getFullYear().toString()+month;
        const yearFormat = d.getFullYear().toString();
        const duration_min = (document.end_time.getTime() - document.start_time.getTime())/60000 //60*1000

        const dayRef = db.collection('statistics').doc(document.cid).collection('day')
            .doc(dayFormat);
        const monthRef = db.collection('statistics').doc(document.cid).collection('month')
            .doc(monthFormat);
        const yearRef = db.collection('statistics').doc(document.cid).collection('year')
            .doc(yearFormat);
    
        //Day 작업
        const getDayDoc = dayRef.get()
        .then(doc => {
            if (!doc.exists) {
                //if document is not exist -> 처음(폴더만듬)

                const data = {
                    behavior_freq: {},
                    summary: {},
                    type: {},
                    reason_type: {},
                    place:{}
                };   

                data["behavior_freq"][d.getHours()] = 1;
                data["summary"]["count"] = 1;
                data["summary"]["duration_min"] = duration_min;
                data["summary"]["intensity_sum"] = document.intensity;
                for (const i in document.type) {
                    data["type"][i] = 1
                }
                for (const i in document.reason) {
                    data["reason_type"][i] = 1
                }
                data["place"][document.place] = 1;

                return dayRef.set(data);

            } else {
                console.log('Document data:', doc.data());

                const trans = db.runTransaction(t => {
                    return t.get(dayRef)
                        .then(docs => {
                        const data = docs.data(); //doc랑 docs랑 같음
                        // Add one person to the city population

                            if (data["behavior_freq"][d.getHours()]) {
                                //값이 있으면
                                data["behavior_freq"][d.getHours()] += 1
                            } else {
                                data["behavior_freq"][d.getHours()] = 1
                            }
                            data["summary"]["count"] += 1;
                            data["summary"]["duration_min"] += duration_min;
                            data["summary"]["intensity_sum"] += document.intensity;
                            for (const i in document.type) {
                                if (data["type"][i]) {
                                    //값이 있으면
                                    data["type"][i] += 1
                                } else {
                                    data["type"][i] = 1
                                }
                            }
                            for (const i in document.reason) {
                                if (data["reason_type"][i]) {
                                    //값이 있으면
                                    data["reason_type"][i] += 1
                                } else {
                                    data["reason_type"][i] = 1
                                }
                            }
                            if (data["place"][document.place]) {
                                data["place"][document.place] += 1;
                            } else {
                                data["place"][document.place] = 1;
                            }

                          t.update(dayRef, data);
                        });
                }).then(result => {
                    console.log('Day Ref Transaction success!');
                }).catch(err => {
                    console.log('Day Ref Transaction failure:', err);
                });

                return null
            }
        })
        .catch(err => {
        console.log('Error getting document', err);
        });

        //Month 작업
        const getMonthDoc = monthRef.get()
        .then(doc => {
            if (!doc.exists) {
                //if document does not exists -> 처음(폴더만듬)

                const data = {
                    behavior_freq: {},
                    summary: {},
                    type: {},
                    reason_type: {},
                    place:{}
                };   

                data["behavior_freq"][d.getDate()] = 1;
                data["summary"]["count"] = 1;
                data["summary"]["duration_min"] = duration_min;
                data["summary"]["intensity_sum"] = document.intensity;
                for (const i in document.type) {
                    data["type"][i] = 1
                }
                for (const i in document.reason) {
                    data["reason_type"][i] = 1
                }
                data["place"][document.place] = 1;

                return monthRef.set(data);

            } else {
                console.log('Document data:', doc.data());
                // if month document exists
                const trans = db.runTransaction(t => {
                    return t.get(monthRef)
                        .then(docs => {
                        const data = docs.data(); //doc랑 docs랑 같음

                            if (data["behavior_freq"][d.getDate()]) {
                                //값이 있으면
                                data["behavior_freq"][d.getDate()] += 1
                            } else {
                                data["behavior_freq"][d.getDate()] = 1
                            }
                            data["summary"]["count"] += 1;
                            data["summary"]["duration_min"] += duration_min;
                            data["summary"]["intensity_sum"] += document.intensity;
                            for (const i in document.type) {
                                if (data["type"][i]) {
                                    //값이 있으면
                                    data["type"][i] += 1
                                } else {
                                    data["type"][i] = 1
                                }
                            }
                            for (const i in document.reason) {
                                if (data["reason_type"][i]) {
                                    //값이 있으면
                                    data["reason_type"][i] += 1
                                } else {
                                    data["reason_type"][i] = 1
                                }
                            }
                            if (data["place"][document.place]) {
                                data["place"][document.place] += 1;
                            } else {
                                data["place"][document.place] = 1;
                            }

                          t.update(monthRef, data);
                        });
                }).then(result => {
                    console.log('Month Ref Transaction success!');
                }).catch(err => {
                    console.log('Month Ref Transaction failure:', err);
                });

                return null
            }
        })
        .catch(err => {
        console.log('Error getting document', err);
        });

        //Year 작업
        const getYearDoc = yearRef.get()
        .then(doc => {
            if (!doc.exists) {
                //if document does not exists -> 처음(폴더만듬)

                const data = {
                    behavior_freq: {},
                    summary: {},
                    type: {},
                    reason_type: {},
                    place:{}
                };   

                data["behavior_freq"][d.getMonth()+1] = 1;
                data["summary"]["count"] = 1;
                data["summary"]["duration_min"] = duration_min;
                data["summary"]["intensity_sum"] = document.intensity;
                for (const i in document.type) {
                    data["type"][i] = 1
                }
                for (const i in document.reason) {
                    data["reason_type"][i] = 1
                }
                data["place"][document.place] = 1;

                return yearRef.set(data);

            } else {
                console.log('Document data:', doc.data());
                // if month document exists
                const trans = db.runTransaction(t => {
                    return t.get(yearRef)
                        .then(docs => {
                        const data = docs.data(); //doc랑 docs랑 같음

                            if (data["behavior_freq"][d.getMonth()+1]) {
                                //값이 있으면
                                data["behavior_freq"][d.getMonth()+1] += 1
                            } else {
                                data["behavior_freq"][d.getMonth()+1] = 1
                            }
                            data["summary"]["count"] += 1;
                            data["summary"]["duration_min"] += duration_min;
                            data["summary"]["intensity_sum"] += document.intensity;
                            for (const i in document.type) {
                                if (data["type"][i]) {
                                    //값이 있으면
                                    data["type"][i] += 1
                                } else {
                                    data["type"][i] = 1
                                }
                            }
                            for (const i in document.reason) {
                                if (data["reason_type"][i]) {
                                    //값이 있으면
                                    data["reason_type"][i] += 1
                                } else {
                                    data["reason_type"][i] = 1
                                }
                            }
                            if (data["place"][document.place]) {
                                data["place"][document.place] += 1;
                            } else {
                                data["place"][document.place] = 1;
                            }

                          t.update(yearRef, data);
                        });
                }).then(result => {
                    console.log('Year Ref Transaction success!');
                }).catch(err => {
                    console.log('Year Ref Transaction failure:', err);
                });

                return null
            }
        })
        .catch(err => {
        console.log('Error getting document', err);
        });

    }
    else {
        console.log("Update Method");
        return null
    }

    // For Debug
    // else {
    //     console.log("Create and Update Method");
    //     const d = new Date(document.start_time._seconds * 1000 + 540 * 60 * 1000);

    //     const dayFormat = d.getFullYear().toString()+(d.getMonth()+1).toString()+d.getDate().toString();
    //     const monthFormat = d.getFullYear().toString()+(d.getMonth()+1).toString();
    //     const yearFormat = d.getFullYear().toString();
    //     const duration_min = (document.end_time._seconds - document.start_time._seconds)/60
    
    //     const dayRef = db.collection('statistics').doc(document.cid).collection('day')
    //         .doc(dayFormat);
    //     const monthRef = db.collection('statistics').doc(document.cid).collection('month')
    //         .doc(monthFormat);
    //     const yearRef = db.collection('statistics').doc(document.cid).collection('year')
    //         .doc(yearFormat);
    
    //     //Day 작업
    //     const getDayDoc = dayRef.get()
    //     .then(doc => {
    //         if (!doc.exists) {
    //             //if document is not exist -> 처음(폴더만듬)

    //             const data = {
    //                 behavior_freq: {},
    //                 summary: {},
    //                 type: {},
    //                 reason_type: {},
    //                 place:{}
    //             };   

    //             data["behavior_freq"][d.getHours()] = 1;
    //             data["summary"]["count"] = 1;
    //             data["summary"]["duration_min"] = duration_min;
    //             data["summary"]["intensity_sum"] = document.intensity;
    //             for (const i in document.type) {
    //                 data["type"][i] = 1
    //             }
    //             for (const i in document.reason) {
    //                 data["reason_type"][i] = 1
    //             }
    //             data["place"][document.place] = 1;

    //             return dayRef.set(data);

    //         } else {
    //             console.log('Document data:', doc.data());

    //             const trans = db.runTransaction(t => {
    //                 return t.get(dayRef)
    //                     .then(docs => {
    //                     const data = docs.data(); //doc랑 docs랑 같음
    //                     // Add one person to the city population

    //                         if (data["behavior_freq"][d.getHours()]) {
    //                             //값이 있으면
    //                             data["behavior_freq"][d.getHours()] += 1
    //                         } else {
    //                             data["behavior_freq"][d.getHours()] = 1
    //                         }
    //                         data["summary"]["count"] += 1;
    //                         data["summary"]["duration_min"] += duration_min;
    //                         data["summary"]["intensity_sum"] += document.intensity;
    //                         for (const i in document.type) {
    //                             if (data["type"][i]) {
    //                                 //값이 있으면
    //                                 data["type"][i] += 1
    //                             } else {
    //                                 data["type"][i] = 1
    //                             }
    //                         }
    //                         for (const i in document.reason) {
    //                             if (data["reason_type"][i]) {
    //                                 //값이 있으면
    //                                 data["reason_type"][i] += 1
    //                             } else {
    //                                 data["reason_type"][i] = 1
    //                             }
    //                         }
    //                         if (data["place"][document.place]) {
    //                             data["place"][document.place] += 1;
    //                         } else {
    //                             data["place"][document.place] = 1;
    //                         }

    //                       t.update(dayRef, data);
    //                     });
    //             }).then(result => {
    //                 console.log('Day Ref Transaction success!');
    //             }).catch(err => {
    //                 console.log('Day Ref Transaction failure:', err);
    //             });

    //             return null
    //         }
    //     })
    //     .catch(err => {
    //     console.log('Error getting document', err);
    //     });

    //     //Month 작업
    //     const getMonthDoc = monthRef.get()
    //     .then(doc => {
    //         if (!doc.exists) {
    //             //if document does not exists -> 처음(폴더만듬)

    //             const data = {
    //                 behavior_freq: {},
    //                 summary: {},
    //                 type: {},
    //                 reason_type: {},
    //                 place:{}
    //             };   

    //             data["behavior_freq"][d.getHours()] = 1;
    //             data["summary"]["count"] = 1;
    //             data["summary"]["duration_min"] = duration_min;
    //             data["summary"]["intensity_sum"] = document.intensity;
    //             for (const i in document.type) {
    //                 data["type"][i] = 1
    //             }
    //             for (const i in document.reason) {
    //                 data["reason_type"][i] = 1
    //             }
    //             data["place"][document.place] = 1;

    //             return monthRef.set(data);

    //         } else {
    //             console.log('Document data:', doc.data());
    //             // if month document exists
    //             const trans = db.runTransaction(t => {
    //                 return t.get(monthRef)
    //                     .then(docs => {
    //                     const data = docs.data(); //doc랑 docs랑 같음

    //                         if (data["behavior_freq"][d.getHours()]) {
    //                             //값이 있으면
    //                             data["behavior_freq"][d.getHours()] += 1
    //                         } else {
    //                             data["behavior_freq"][d.getHours()] = 1
    //                         }
    //                         data["summary"]["count"] += 1;
    //                         data["summary"]["duration_min"] += duration_min;
    //                         data["summary"]["intensity_sum"] += document.intensity;
    //                         for (const i in document.type) {
    //                             if (data["type"][i]) {
    //                                 //값이 있으면
    //                                 data["type"][i] += 1
    //                             } else {
    //                                 data["type"][i] = 1
    //                             }
    //                         }
    //                         for (const i in document.reason) {
    //                             if (data["reason_type"][i]) {
    //                                 //값이 있으면
    //                                 data["reason_type"][i] += 1
    //                             } else {
    //                                 data["reason_type"][i] = 1
    //                             }
    //                         }
    //                         if (data["place"][document.place]) {
    //                             data["place"][document.place] += 1;
    //                         } else {
    //                             data["place"][document.place] = 1;
    //                         }

    //                       t.update(monthRef, data);
    //                     });
    //             }).then(result => {
    //                 console.log('Month Ref Transaction success!');
    //             }).catch(err => {
    //                 console.log('Month Ref Transaction failure:', err);
    //             });

    //             return null
    //         }
    //     })
    //     .catch(err => {
    //     console.log('Error getting document', err);
    //     });

    //     //Year 작업
    //     const getYearDoc = yearRef.get()
    //     .then(doc => {
    //         if (!doc.exists) {
    //             //if document does not exists -> 처음(폴더만듬)

    //             const data = {
    //                 behavior_freq: {},
    //                 summary: {},
    //                 type: {},
    //                 reason_type: {},
    //                 place:{}
    //             };   

    //             data["behavior_freq"][d.getHours()] = 1;
    //             data["summary"]["count"] = 1;
    //             data["summary"]["duration_min"] = duration_min;
    //             data["summary"]["intensity_sum"] = document.intensity;
    //             for (const i in document.type) {
    //                 data["type"][i] = 1
    //             }
    //             for (const i in document.reason) {
    //                 data["reason_type"][i] = 1
    //             }
    //             data["place"][document.place] = 1;

    //             return yearRef.set(data);

    //         } else {
    //             console.log('Document data:', doc.data());
    //             // if month document exists
    //             const trans = db.runTransaction(t => {
    //                 return t.get(yearRef)
    //                     .then(docs => {
    //                     const data = docs.data(); //doc랑 docs랑 같음

    //                         if (data["behavior_freq"][d.getHours()]) {
    //                             //값이 있으면
    //                             data["behavior_freq"][d.getHours()] += 1
    //                         } else {
    //                             data["behavior_freq"][d.getHours()] = 1
    //                         }
    //                         data["summary"]["count"] += 1;
    //                         data["summary"]["duration_min"] += duration_min;
    //                         data["summary"]["intensity_sum"] += document.intensity;
    //                         for (const i in document.type) {
    //                             if (data["type"][i]) {
    //                                 //값이 있으면
    //                                 data["type"][i] += 1
    //                             } else {
    //                                 data["type"][i] = 1
    //                             }
    //                         }
    //                         for (const i in document.reason) {
    //                             if (data["reason_type"][i]) {
    //                                 //값이 있으면
    //                                 data["reason_type"][i] += 1
    //                             } else {
    //                                 data["reason_type"][i] = 1
    //                             }
    //                         }
    //                         if (data["place"][document.place]) {
    //                             data["place"][document.place] += 1;
    //                         } else {
    //                             data["place"][document.place] = 1;
    //                         }

    //                       t.update(yearRef, data);
    //                     });
    //             }).then(result => {
    //                 console.log('Year Ref Transaction success!');
    //             }).catch(err => {
    //                 console.log('Year Ref Transaction failure:', err);
    //             });

    //             return null
    //         }
    //     })
    //     .catch(err => {
    //     console.log('Error getting document', err);
    //     });
        
    // }

});















            // //Create라고 가정하고 만듬(update도 create로 침 지금음)
            // const data = doc.data();

            // // return db.runTransaction(t => {
            // //     const userCounterRef = db.collection("users").doc("userCounter")
            // //     return t.get(userCounterRef).then(doc => {
            // //         // Add one user to the userCounter
            // //         var newUserCounter = doc.data().userCounter + 1
            // //         t.update(userCounterRef, { userCounter: newUserCounter })
            // //         // And update the user's own doc
            // //         const userDoc = db.collection("users").doc(userId)
            // //         t.set(userDoc, { "userId" : userId })
            // //     })
            // // })

            // // var trans = db.runTransaction(t => {
            // //     return t.get(dayRef)
            // //         .then(doc => {
            // //           // Add one person to the city population
            // //         //   var newPopulation = data.behavior_freq[d.getHours()] + 1;//doc.data().population + 1;
            // //         //   t.update(dayRef, { population: newPopulation });
            // //         });
            // //   }).then(result => {
            // //     console.log('Transaction success!');
            // //   }).catch(err => {
            // //     console.log('Transaction failure:', err);
            // //   });


    exports.sendNotification = functions.firestore
    .document('behaviors/{behaviorId}')
    .onCreate((snap, context) => {

      const newValue = snap.data();

      const uid = newValue.uid;
      const cid = newValue.cid;

      // The snapshot to the user's tokens.
      let tokensSnapshot;
      let tokens_to_send_to;
      let getDeviceTokensPromise;

      db.doc(`users/${uid}`).get().then((result => {

	    getDeviceTokensPromise = result.data().notificationTokens;
        
        db.doc(`childs/${cid}`).get().then((results => {

		    tokensSnapshot = results.data().users;
	        tokens_to_send_to = [];

	        console.log("작동중");

	        for(const i in tokensSnapshot) {
	        	const map = tokensSnapshot[i];

	        	for(const obj in map){
	        		if (map.hasOwnProperty(obj)) {
				        if(obj === 'notificationTokens') {
				        	if(obj !== getDeviceTokensPromise) {
				        		console.log(map[obj]);
				        		tokens_to_send_to.push(map[obj]);
				        	}
				        }
				    }
	        	}
	        }

		    // Notification details.
	        const payload = {
	          notification: {
	            title: '지니어티(Geniauti) 알림',
	            body: `아이의 새로운 행동 기록이 추가되었습니다.`,
	            // icon: follower.photoURL
	          }
	        };

		    // Send notifications to all tokens.
		    if(tokens_to_send_to.length === 0) {
		    	return null;
		    } else {
		    	return admin.messaging().sendToDevice(tokens_to_send_to, payload);
		    }
	       

			})).catch((error) => {
		    	console.log(error);
		    })

		return null;

		})).catch((error) => {
	    	console.log(error);
	    })

	    return null;
    
    });