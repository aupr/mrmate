# mrmate
Modbus REST Mate (mrmate) is a software to establish Modbus (TCP, RTU, ASCII) communication over RESTful service


#### Key Features:
- Consume and produce XML or JSON data format according to request header
- CORS header response with wildcard for hassle free development
- Automatic communication connection recovery
- Easy host port setup option



#### Read Data
HTTP GET Method

##### URL Pattern For Modbus Bit
- Read Single Coil <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/0/{offset}`
- Read Multiple Coils <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/0/{offset}/{count}`
- Read Single Discrete Input <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/1/{offset}`
- Read Multiple Discrete Inputs <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/1/{offset}/{count}`

##### URL Pattern For Modbus Register
- Read Single Input Register <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/3/{offset}`
- Read Multiple Input Registers <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/3/{offset}/{count}`
- Read Single Holding Register <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/4/{offset}`
- Read Multiple Holding Registers <br />
`http://{host address}:{port}/mrmate/read/{uri name}/{unit id}/4/{offset}/{count}`

Example to Read 10 Holding Registers: `http://localhost:505/mrmate/read/uriname/1/4/1/10` 

#### Write Data
HTTP POST Method

##### URL Pattern For Modbus Bit
XML Tags: `<modbusBits>`, `<modbusBit>`, `<value>`
- Write Single Coil <br />
`http://{host address}:{port}/mrmate/write/{uri name}/{unit id}/0/{offset}`
- Write Multiple Coils <br />
`http://{host address}:{port}/mrmate/write/{uri name}/{unit id}/0/{offset}/multi`

##### URL Pattern For Modbus Register
XML Tags: `<modbusRegisters>`, `<modbusRegister>`, `<value>`
- Write Single Holding Register <br />
`http://{host address}:{port}/mrmate/write/{uri name}/{unit id}/4/{offset}`
- Write Multiple Holding Registers <br />
`http://{host address}:{port}/mrmate/write/{uri name}/{unit id}/4/{offset}/multi`


#### POST Data Pattern for XML Format
- For Single Coil <br />
```
<modbusBit>
    <value>{Boolean Value}</value>
</modbusBit>
```
- For Multiple Coil <br />
```
<modbusBits>
    <modbusBit>
        <value>{Boolean Value}</value>
    </modbusBit>
    <modbusBit>
        <value>{Boolean Value}</value>
    </modbusBit>
    <modbusBit>
        <value>{Boolean Value}</value>
    </modbusBit>
    ..................
    ..................
</modbusBits>
```


- For Single Register <br />
```
<modbusRegister>
    <value>{16 Bit Integer Value}</value>
</modbusRegister>
```
- For Multiple Registers <br />
```
<modbusRegisters>
    <modbusRegister>
        <value>{16 Bit Integer Value}</value>
    </modbusRegister>
    <modbusRegister>
        <value>{16 Bit Integer Value}</value>
    </modbusRegister>
    <modbusRegister>
        <value>{16 Bit Integer Value}</value>
    </modbusRegister>
    ..................
    ..................
</modbusRegisters>
```

#### POST Data Pattern for JSON Format
- For Single Coil <br />
```
{
    "value": <Boolean value>
}
```
- For Multiple Coil <br />
```
[
    {"value":<Boolean value>},
    {"value":<Boolean value>},
    {"value":<Boolean value>},
    .........................
    .........................
]
```


- For Single Register <br />
```
{
    "value": <16 Bit Integer Value>
}
```
- For Multiple Registers <br />
```
[
    {"value": <16 Bit Integer Value>},
    {"value": <16 Bit Integer Value>},
    {"value": <16 Bit Integer Value>},
    ...............................
    ...............................
]
```

Example to Write 5 Holding Registers: <br />
Uri: `http://localhost:505/mrmate/write/uriname/1/4/1/multi`
Body Data XML:
```
<modbusRegisters>
    <modbusRegister>
        <value>111</value>
    </modbusRegister>
    <modbusRegister>
        <value>222</value>
    </modbusRegister>
    <modbusRegister>
        <value>333</value>
    </modbusRegister>
    <modbusRegister>
        <value>444</value>
    </modbusRegister>
    <modbusRegister>
        <value>444</value>
    </modbusRegister>
</modbusRegisters>
```

Body Data JSON:
```
[
    {"value": 111},
    {"value": 222},
    {"value": 333},
    {"value": 444},
    {"value": 555}
]
```