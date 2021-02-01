db.createUser(
    {
        user: "user1",
        pwd: "pwd1",
        roles: [
            {
                role: "readWrite",
                db: "mydb"
            }
        ]
    }
);

/*
db.products.save({
title:'Learning English',
description:'a very good book for kids',
price: 9.99,
stock: 10,
categories: [{name: 'book'}]
})

db.locations.save({
name:'Starcups',
address: '125 High street',
rating: 3,
facilities: ['Food'],
coords: [-0.96, 51.45],
openingTimes: [{
days: 'Monday - Friday',
opening: '7AM',
closing: '7PM',
closed: false},{
days: 'Saturday',
opening: '8AM',
closing: '5PM',
closed: false},{
days: 'Sunday',
closed: true}]
})

db.locations.save({
name:'StarBucks',
address: '1315 High street',
rating: 3,
facilities: ['Food'],
coords: [45.50155, 73.56735],
openingTimes: [{
days: 'Monday - Friday',
opening: '7AM',
closing: '7PM',
closed: false},{
days: 'Saturday',
opening: '8AM',
closing: '5PM',
closed: false},{
days: 'Sunday',
closed: true}]
})

db.locations.update({
name: 'Starcups'
},{
$push: {
reviews: {
author: 'T Gao',
_id: ObjectId(),
rating: 3,
timestamp: new Date("Jan 26 2017"),
reviewText: "Horrible"
}
}
})
*/