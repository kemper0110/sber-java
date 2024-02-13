import React, {useEffect, useState} from 'react';
import {TextInput} from '@mantine/core';
import {IconSearch} from '@tabler/icons-react';
import {Loader} from '@mantine/core';

import {Avatar, Badge, Table, Group, Text, ActionIcon, Anchor, rem} from '@mantine/core';
import {IconPencil, IconTrash} from '@tabler/icons-react';


/**
 * @type {{manager: string, designer: string, engineer: string}}
 */
const jobColors = {
    engineer: 'blue',
    manager: 'cyan',
    designer: 'pink',
};

function App() {
    const [users, setUsers] = useState([
        {
            avatar:
                'https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-1.png',
            name: 'Robert Wolfkisser',
            job: 'Engineer',
            email: 'rob_wolf@gmail.com',
            phone: '+44 (452) 886 09 12',
        },
        {
            avatar:
                'https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-7.png',
            name: 'Jill Jailbreaker',
            job: 'Engineer',
            email: 'jj@breaker.com',
            phone: '+44 (934) 777 12 76',
        },
        {
            avatar:
                'https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-2.png',
            name: 'Henry Silkeater',
            job: 'Designer',
            email: 'henry@silkeater.io',
            phone: '+44 (901) 384 88 34',
        },
        {
            avatar:
                'https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-3.png',
            name: 'Bill Horsefighter',
            job: 'Designer',
            email: 'bhorsefighter@gmail.com',
            phone: '+44 (667) 341 45 22',
        },
        {
            avatar:
                'https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-10.png',
            name: 'Jeremy Footviewer',
            job: 'Manager',
            email: 'jeremy@foot.dev',
            phone: '+44 (881) 245 65 65',
        },
    ]);
    const [query, setQuery] = useState("")
    const [loading, setLoading] = useState(false)
    const onSubmit = (e) => {
        e.preventDefault()
        console.log('send', query)
        setLoading(true)
        setTimeout(() => {
            setLoading(false)
        }, 500)
    }

    return (
        <div className={'h-screen w-full flex place-items-center justify-center'}>
            <div className={'flex flex-col items-center'}>
                <form onSubmit={onSubmit}>
                    <TextInput
                        value={query}
                        onChange={e => setQuery(e.currentTarget.value)}
                        w={800}
                        size={'xl'}
                        leftSectionPointerEvents="pointer"
                        leftSection={<button onClick={onSubmit}><IconSearch/></button>}
                        rightSectionPointerEvents="none"
                        rightSection={loading ? <Loader color="blue" size={'sm'} type={'dots'}/> : null}
                        label="Поиск пользователя по имени"
                        placeholder="Имя для поиска"
                    />
                </form>
                <Table.ScrollContainer minWidth={800} mt={20}>
                    <Table verticalSpacing="sm">
                        <Table.Thead>
                            <Table.Tr>
                                <Table.Th>Пользователь</Table.Th>
                                <Table.Th>Название работы</Table.Th>
                                <Table.Th>Почта</Table.Th>
                                <Table.Th>Телефон</Table.Th>
                                <Table.Th/>
                            </Table.Tr>
                        </Table.Thead>
                        <Table.Tbody>
                            {
                                users.map(user => (
                                    <Table.Tr key={user.name}>
                                        <Table.Td>
                                            <Group gap="sm">
                                                <Avatar size={30} src={user.avatar} radius={30}/>
                                                <Text fz="sm" fw={500}>
                                                    {user.name}
                                                </Text>
                                            </Group>
                                        </Table.Td>
                                        <Table.Td>
                                            <Badge color={jobColors[user.job.toLowerCase()]} variant="light">
                                                {user.job}
                                            </Badge>
                                        </Table.Td>
                                        <Table.Td>
                                            <Anchor component="button" size="sm">
                                                {user.email}
                                            </Anchor>
                                        </Table.Td>
                                        <Table.Td>
                                            <Text fz="sm">{user.phone}</Text>
                                        </Table.Td>
                                        <Table.Td>
                                            <Group gap={0} justify="flex-end">
                                                <ActionIcon variant="subtle" color="gray">
                                                    <IconPencil style={{width: rem(16), height: rem(16)}} stroke={1.5}/>
                                                </ActionIcon>
                                                <ActionIcon variant="subtle" color="red">
                                                    <IconTrash style={{width: rem(16), height: rem(16)}} stroke={1.5}/>
                                                </ActionIcon>
                                            </Group>
                                        </Table.Td>
                                    </Table.Tr>
                                ))
                            }
                        </Table.Tbody>
                    </Table>
                </Table.ScrollContainer>
            </div>
        </div>
    )
}

export default App
