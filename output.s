push 0
push 0
push 0
push 0
st
push 1
push 1
st
L0:
push 1
ld
push 4
leq
jz L0end
push 0
push 0
ld
push 1
ld
push 1
ld
mul
add
st
push 1
push 1
ld
push 1
add
st
jmp L0
L0end:
push 0
ld
end
