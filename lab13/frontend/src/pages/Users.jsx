import React, {useRef, useState} from 'react';
import {Link} from "react-router-dom";
import {ActionIcon, Anchor, Avatar, Badge, Group, Loader, rem, Table, Text, TextInput} from '@mantine/core';
import {IconPencil, IconSearch, IconTrash} from '@tabler/icons-react';


const getAvatar = id => `https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/avatars/avatar-${id}.png`

const useUserQuery = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false)
    const source = useRef(null)
    const onSubmit = (name) => {
        setUsers([])
        console.log('send', name)
        setLoading(true)

        if(source.current)
            source.current.close()
        source.current = new EventSource(`/api/user?q=${encodeURIComponent(name)}`)
        source.current.onmessage = e => {
            setUsers(users => [...users, JSON.parse(e.data)])
        }
        source.current.onerror = e => {
            console.info("err", e, e.type)
            setLoading(false)
            source.current.close()
        }
    }
    return {
        isLoading: loading,
        users,
        submit: onSubmit
    }
}


export default function Users() {
    const [query, setQuery] = useState("")
    const {isLoading, users, submit} = useUserQuery()

    const usersWithImages = users.map(user => ({
        ...user,
        image: getAvatar(Number.parseInt(user.image) + 1)
    }))

    const onSubmit = e => {
        e.preventDefault()
        submit(query)
    }


    return (
        <div className={'min-h-screen w-full'}>
            <div className={'mt-20 flex flex-col items-center'}>
                <Link to={"/"} className={'text-blue-400'}>
                    Readme
                </Link>
                <form onSubmit={onSubmit} >
                    <TextInput
                        value={query}
                        onChange={e => setQuery(e.currentTarget.value)}
                        w={800}
                        size={'xl'}
                        leftSectionPointerEvents="pointer"
                        leftSection={<button onClick={onSubmit}><IconSearch/></button>}
                        rightSectionPointerEvents="none"
                        rightSection={isLoading ? <Loader color="blue" size={'sm'}/> : null}
                        label="Поиск пользователя по имени"
                        placeholder="Имя для поиска"
                    />
                </form>
                <Table.ScrollContainer minWidth={800} mt={20} w={1000}>
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
                                usersWithImages.map(user => (
                                    <Table.Tr key={user.id}>
                                        <Table.Td>
                                            <Group gap="sm">
                                                <Avatar size={30} src={user.image} radius={30}/>
                                                <Text fz="sm" fw={500}>
                                                    {user.firstname} {user.lastname}
                                                </Text>
                                            </Group>
                                        </Table.Td>
                                        <Table.Td>
                                            <Badge variant="light">
                                                {user.job}
                                            </Badge>
                                        </Table.Td>
                                        <Table.Td>
                                            <Anchor component="button" size="sm">
                                                {user.mail}
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