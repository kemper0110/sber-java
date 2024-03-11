export function SearchedText({text, needle}: {
    text: string
    needle: string
}) {
    console.log("searching", needle, 'in', text)
    const pattern = '(' + needle.replace(/[.*+?^${}()|[\]\\]/g, '\\$&') + ')';
    const regex = new RegExp(pattern, 'gi');
    const parts = text.split(regex)
    console.log({parts})
    return (
        <span>
            {
                parts.map((part, index) => (
                    index % 2 === 0 ? part : (
                        <mark key={index} className={'bg-yellow-300'}>
                            {part}
                        </mark>
                    )
                ))
            }
        </span>
    )
}