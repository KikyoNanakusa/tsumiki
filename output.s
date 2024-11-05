push 0
push 0
push 0
push 0
push 3
st
push 1
push 3
st
push 0
ld
push 1
ld
eq
jz L0end
push 2
push 5
st
push 2
ld
end
L0end:
push 1
end
L1:
push 0
ld
push 1
ld
eq
jz L1end
push 2
push 5
st
push 2
ld
end
jmp L1
L1end:
