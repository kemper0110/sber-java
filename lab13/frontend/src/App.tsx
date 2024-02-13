import React, {useEffect} from 'react';
import {TextInput} from '@mantine/core';
import {IconSearch} from '@tabler/icons-react';


function App() {
    useEffect(() => {
    }, []);
    const onSubmit = (e: SubmitEvent) => {
        e.preventDefault()

    }

    return (
      <div className={'h-screen w-full flex place-items-center justify-center'}>
          <div className={'flex flex-col items-center'}>
              <form onSubmit={onSubmit}>
                  <TextInput
                      w={800}
                      size={'xl'}
                      leftSectionPointerEvents="none"
                      leftSection={<IconSearch/>}
                      label="Поиск пользователя по имения"
                      placeholder="Имя для поиска"
                  />
              </form>
          </div>
      </div>
  )
}

export default App
